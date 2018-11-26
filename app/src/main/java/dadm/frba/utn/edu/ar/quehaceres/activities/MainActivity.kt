package dadm.frba.utn.edu.ar.quehaceres.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.adapters.GroupsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.widget.DividerItemDecoration
import com.google.android.gms.common.util.CollectionUtils.isEmpty
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val services by lazy { Services(this) }

    val adapter: GroupsAdapter by lazy {
        GroupsAdapter {
            startActivity(GroupActivity.newIntent(this@MainActivity, it))
        }
    }

    val grupoTrucho: Api.Group = Api.Group(id = 100, name = "Este grupo no es de verdad. Aca habria un grupo si hubieras creado uno. Toca el icono con el simbolo '+'", members = listOf(), tasks = listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
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

        val user = services.currentUser()
        with(nav_view.getHeaderView(0)) {
            avatar.setImageURI(user!!.avatar)
            full_name.text = user.fullName
            username.text = user.email
        }
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        services.myGroups()
            .doOnSubscribe { showLoading(true) }
            .subscribe(
                    {
                        showLoading(false)

                        if (isEmpty(it)){
                            adapter.groups = listOf(grupoTrucho)
                            adapter.notifyDataSetChanged()
                        }
                        else {
                            adapter.groups = it
                            adapter.notifyDataSetChanged()
                            }
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
        when (item.itemId) {
            R.id.logout -> {
                services.logout(this)
                        .subscribe { it.startActivities() }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
