package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.fragments.NotificationsFragment
import java.lang.IllegalStateException

class NotificationsActivity : AppCompatActivity(), NotificationsFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications_activity)
        val groupId = intent.extras?.getInt(ARG_GROUP_ID) ?: throw IllegalStateException("A group id must be provided")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NotificationsFragment.newInstance(groupId))
                    .commitNow()
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onListFragmentInteraction(item: Api.Notification) {
    }

    companion object {
        const val ARG_GROUP_ID = "group-id"

        fun newIntent(context: Context, groupId: Int): Intent {
            val intent = Intent(context, NotificationsActivity::class.java)
            intent.putExtra(ARG_GROUP_ID, groupId)
            return intent
        }
    }
}
