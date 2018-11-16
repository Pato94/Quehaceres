package dadm.frba.utn.edu.ar.quehaceres.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.adapters.GroupsAdapter
import dadm.frba.utn.edu.ar.quehaceres.services.GroupsService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.widget.DividerItemDecoration
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    val groupsService: GroupsService by lazy { GroupsService() }
    val api by lazy { Api().api }

    val adapter: GroupsAdapter by lazy {
        GroupsAdapter {
            startActivity(GroupActivity.newIntent(it, this@MainActivity))
        }
    }



 //   val idUsuario: Int = id!!
 //   val idUsuarioInt: Int = idUsuario.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivity(CreateGroupActivity.newIntent(this))
        }

        recycler_view.adapter = adapter

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recycler_view.addItemDecoration(itemDecor)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()


        val bundle: Bundle? = intent.extras
        val id = bundle!!.getInt("id")
//        groupsService.getGroups()
            api.myGroups(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading(true) }
                .subscribe(
                        {
                            showLoading(false)
                            adapter.groups = it
                            adapter.notifyDataSetChanged()
                        },
                        {
                            showLoading(false)
                            Toast.makeText(this, "Hubo un error al cargar los grupos", Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun showLoading(loading: Boolean) {
        // TODO
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
