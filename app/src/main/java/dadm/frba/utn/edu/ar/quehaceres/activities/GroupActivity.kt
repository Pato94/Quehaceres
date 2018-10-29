package dadm.frba.utn.edu.ar.quehaceres.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.AvailableTasksFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.MyTasksFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent
import dadm.frba.utn.edu.ar.quehaceres.models.Group
import kotlinx.android.synthetic.main.activity_group.*

class GroupActivity : AppCompatActivity(), AvailableTasksFragment.Listener, MyTasksFragment.Listener {

  override fun onMyTaskClicked(item: DummyContent.DummyItem?) {
    item?.let {
      AlertDialog.Builder(this)
          .setTitle(it.id)
          .setMessage(it.content)
          .setPositiveButton("Verificar") { _, _ -> onVerifyClicked() }
          .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
          .show()
    }
  }

  override fun onAvailableTaskClicked(item: DummyContent.DummyItem?) {
    item?.let {
      AlertDialog.Builder(this)
          .setTitle(it.id)
          .setMessage(it.content)
          .setPositiveButton("Asignar") { _, _ -> Toast.makeText(this, "Asignar clicked", Toast.LENGTH_SHORT).show() }
          .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
          .show()
    }
  }

  companion object {
    const val CAMERA_REQUEST_ID = 1001

    fun newIntent(group: Group, context: Context): Intent {
      val intent = Intent(context, GroupActivity::class.java)
      intent.putExtra("GROUP", group)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_group)

    setUpViews()
  }

  private fun setUpViews() {
    val group = intent.getParcelableExtra<Group>("GROUP")
    setSupportActionBar(toolbar)
    supportActionBar?.title = group.name

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
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_ID)
      }
    } else {
      startVerification()
    }
  }

  private fun startVerification() {
    Toast.makeText(this, "Tenemos permiso", Toast.LENGTH_SHORT).show()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when (requestCode) {
      CAMERA_REQUEST_ID -> {
        // If request is cancelled, the result arrays are empty.
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          startVerification()
        } else {
          Toast.makeText(this, "No tenemos permiso", Toast.LENGTH_SHORT).show()
        }
        return
      }
      else -> { }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_group, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    if (id == R.id.action_notifications) {
      val intent = NotificationsActivity.newIntent(this)
      startActivity(intent)
      return true
    }

    return super.onOptionsItemSelected(item)
  }

  inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
      return when (position) {
        0 -> AvailableTasksFragment.newInstance(1)
        else -> MyTasksFragment.newInstance(1)
      }
    }

    override fun getCount(): Int {
      return 2
    }
  }
}
