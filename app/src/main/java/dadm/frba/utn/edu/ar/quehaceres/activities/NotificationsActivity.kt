package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.NotificationsFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.Notification

class NotificationsActivity : AppCompatActivity(), NotificationsFragment.OnListFragmentInteractionListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NotificationsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NotificationsFragment.newInstance(1))
                    .commitNow()
        }
    }

    override fun onListFragmentInteraction(item: Notification.NotificationItem?) {
    }
}
