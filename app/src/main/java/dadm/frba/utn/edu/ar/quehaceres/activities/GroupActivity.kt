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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.OnTaskAssigned
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.fragments.AvailableTasksFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.MyTasksFragment
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.activity_group.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalStateException

class GroupActivity : AppCompatActivity(), AvailableTasksFragment.Listener, MyTasksFragment.Listener {

    val services by lazy { Services(this) }
    var group: Api.Group? = null
    private var eventBus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = intent.getParcelableExtra("GROUP")

        if (group == null) {
            throw IllegalStateException("Group cannot be null")
        }

        setContentView(R.layout.activity_group)
        setUpViews()
    }

    private fun setUpViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = group!!.name

        setUpAdapter()
    }

    private fun setUpAdapter() {
        container.adapter = SectionsPagerAdapter(supportFragmentManager)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    private fun onVerifyClicked() {
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
                confirmImage(imageBitmap)
            }
        }
    }

    private fun confirmImage(imageBitmap: Bitmap) {
        val view = ImageView(this)
        view.setImageBitmap(imageBitmap)

        AlertDialog.Builder(this)
                .setTitle("Esta imagen es correcta?")
                .setView(view)
                .setPositiveButton("Confirmar", { _, _ -> Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show() })
                .setNegativeButton("Cancelar", { d, _ -> d.dismiss() })
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
            val intent = NotificationsActivity.newIntent(this)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMyTaskClicked(item: Api.Task) {
        AlertDialog.Builder(this)
                .setTitle(item.name)
                .setMessage("Querés verificar esta tarea?")
                .setPositiveButton("Verificar") { _, _ -> onVerifyClicked() }
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
        const val CAMERA_PERMISSION_REQUEST = 1001
        const val CAPTURE_IMAGE_REQUEST = 1002

        fun newIntent(group: Api.Group, context: Context): Intent {
            val intent = Intent(context, GroupActivity::class.java)
            intent.putExtra("GROUP", group)
            return intent
        }
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AvailableTasksFragment.newInstance(group!!.id)
                else -> MyTasksFragment.newInstance(group!!.id)
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
