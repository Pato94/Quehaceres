package dadm.frba.utn.edu.ar.quehaceres.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.facebook.drawee.view.SimpleDraweeView
import dadm.frba.utn.edu.ar.quehaceres.*
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.fragments.*
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.activity_group.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalStateException

class GroupActivity : AppCompatActivity(), AvailableTasksFragment.Listener, MyTasksFragment.Listener {

    val services by lazy { Services(this) }
    var group: Api.Group? = null
    private var eventBus = EventBus.getDefault()
    private var takingPhotoFromTask: Api.Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = (savedInstanceState ?: intent.extras).getParcelable(ARG_GROUP)

        if (group == null) {
            throw IllegalStateException("Group cannot be null")
        }

        setContentView(R.layout.activity_group)
        setUpViews()
    }

    private fun setUpViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = group!!.name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpAdapter()

        new_task.setOnClickListener {
            CreateTaskDialog(this, maxPoints(), ::createTask).show()
        }
    }

    private fun maxPoints(): Int {
        return group!!.members.first { it.id == services.currentUser()!!.id }.points + 100
    }

    @SuppressLint("CheckResult")
    private fun createTask(name: String, reward: Int) {
        val currentId = services.currentUser()!!.id
        group = group!!.copy(
                members = group!!.members.map {
                    if (currentId == it.id) it.copy(points = it.points - reward + 100)
                    else it
                })

        eventBus.post(OnGroupUpdated(group!!))

        services.createTask(group!!.id, name, reward)
                .subscribe(
                        { eventBus.post(OnTaskCreated()) },
                        { it.printStackTrace() }
                )
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(ARG_GROUP, group!!)
    }

    private fun setUpAdapter() {
        container.adapter = SectionsPagerAdapter(supportFragmentManager)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        container.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 0) new_task.show()
                else new_task.hide()
            }

            override fun onPageScrollStateChanged(p0: Int) {
            }
        })
    }

    private fun onVerifyClicked(item: Api.Task) {
        takingPhotoFromTask = item
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // TODO
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
            }
        } else {
            startVerification()
        }
    }

    private fun startVerification() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK -> {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val imageBitmap = data!!.extras.get("data") as Bitmap
                confirmImage(imageBitmap, takingPhotoFromTask!!)
            }
        }
        takingPhotoFromTask = null
    }

    /*
    This is probably the most disgusting code you'll ever see
    ...But it's 3 am and i'm just too tired for this shit
     */
    @SuppressLint("CheckResult")
    private fun confirmImage(imageBitmap: Bitmap, actualTask: Api.Task) {
        val layout = RelativeLayout(this)
        val image = SimpleDraweeView(this)
        val progress = ProgressBar(this)
        layout.addView(image)
        layout.addView(progress)
        image.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        progress.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        image.visibility = View.GONE
        image.hierarchy.setProgressBarImage(CircularProgressDrawable(this))

        var callback = { Toast.makeText(this, "Please wait for the upload to finish", Toast.LENGTH_SHORT).show() }
        val verification: (String) -> () -> Unit = { url: String ->
            {
                services.verifyTask(group!!.id, actualTask.id, url)
                        .subscribe(
                                { eventBus.post(OnTaskVerified()) },
                                { Toast.makeText(this, "Error verificating task", Toast.LENGTH_SHORT).show() }
                        )
            }
        }

        services.upload(imageBitmap)
                .subscribe(
                        {
                            progress.visibility = View.GONE
                            image.visibility = View.VISIBLE
                            image.setImageURI(it.file)
                            callback = verification(it.file)
                        },
                        {
                            Toast.makeText(this, "Error while uploading an image", Toast.LENGTH_SHORT).show()
                        }
                )

        AlertDialog.Builder(this)
                .setTitle("Esta imagen es correcta?")
                .setView(layout)
                .setPositiveButton("Confirmar") { _, _ -> callback() }
                .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startVerification()
                } else {
                    Toast.makeText(this, "No tenemos permiso", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_group, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_notifications) {
            val intent = NotificationsActivity.newIntent(this, group!!.id)
            startActivity(intent)
            return true
        } else if (id == R.id.action_invite) {
            InvitePeopleDialog(this, group!!.id).show()
            return true
        } else if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMyTaskClicked(item: Api.Task) {
        AlertDialog.Builder(this)
                .setTitle(item.name)
                .setMessage("Querés verificar esta tarea?")
                .setPositiveButton("Verificar") { _, _ -> onVerifyClicked(item) }
                .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
                .show()
    }

    override fun onAvailableTaskClicked(item: Api.Task) {
        AlertDialog.Builder(this)
                .setTitle(item.name)
                .setMessage("Querés asignarte esta tarea?")
                .setPositiveButton("Asignar") { _, _ -> assignTask(item) }
                .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
                .show()
    }

    @SuppressLint("CheckResult")
    private fun assignTask(item: Api.Task) {
        services.assignTask(group!!.id, item.id)
                .subscribe(
                        {
                            eventBus.post(OnTaskAssigned())
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    companion object {
        const val ARG_GROUP = "GROUP"
        const val CAMERA_PERMISSION_REQUEST = 1001
        const val CAPTURE_IMAGE_REQUEST = 1002

        fun newIntent(context: Context, group: Api.Group): Intent {
            val intent = Intent(context, GroupActivity::class.java)
            intent.putExtra(ARG_GROUP, group)
            return intent
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AvailableTasksFragment.newInstance(group!!.id)
                1 -> MyTasksFragment.newInstance(group!!.id)
                else -> GroupFragment.newInstance(group!!)
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
