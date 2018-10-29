package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
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
        AlertDialog.Builder(this)
            .setMessage(item!!.content)
            .setPositiveButton("Verificar") { _, _ -> Toast.makeText(this, "Verificar clicked", Toast.LENGTH_SHORT).show() }
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .show()
    }

    override fun onAvailableTaskClicked(item: DummyContent.DummyItem?) {
        AlertDialog.Builder(this)
            .setMessage(item!!.content)
            .setPositiveButton("Asignar") { _, _ -> Toast.makeText(this, "Asignar clicked", Toast.LENGTH_SHORT).show() }
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .show()
    }

    companion object {
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
