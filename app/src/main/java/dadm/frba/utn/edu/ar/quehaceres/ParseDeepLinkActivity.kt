package dadm.frba.utn.edu.ar.quehaceres

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import dadm.frba.utn.edu.ar.quehaceres.activities.GroupActivity
import dadm.frba.utn.edu.ar.quehaceres.activities.LoginActivity
import dadm.frba.utn.edu.ar.quehaceres.activities.MainActivity
import dadm.frba.utn.edu.ar.quehaceres.activities.NotificationsActivity
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import io.reactivex.Observable

class ParseDeepLinkActivity : AppCompatActivity() {

    val services by lazy { Services(this) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parse_deep_link)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        val deeplink: String? = (intent.data ?: intent.extras?.get("deeplink")).toString()

        routeUser(this, deeplink)
                .subscribe { taskBuilder -> taskBuilder.startActivities() }
    }

    companion object {
        fun newIntent(context: Context, deeplink: String): Intent {
            val intent = Intent(context, ParseDeepLinkActivity::class.java)
            intent.data = Uri.parse(deeplink)
            return intent
        }

        fun routeUser(context: Context, deeplink: String?): Observable<TaskStackBuilder> {
            val services = Services(context)
            val stackBuilder = TaskStackBuilder.create(context)

            if (!services.isLoggedIn()) {
                stackBuilder.addNextIntent(LoginActivity.newIntent(context, deeplink))
                return Observable.just(stackBuilder)
            }

            stackBuilder.addNextIntent(MainActivity.newIntent(context))

            if (deeplink == null) {
                return Observable.just(stackBuilder)
            }

            // The uri I want to parse is quehaceres://deeplink/groups/1/notifications
            // I want to output a path like MainActivity -> GroupActivity (With a group) -> Notifications
            val notificationsRegex = Regex("quehaceres://deeplink/groups/(\\d+)/notifications")
            if (deeplink.matches(notificationsRegex)) {
                val (groupId) = notificationsRegex.find(deeplink)!!.destructured
                return services.myGroups()
                        .map { groups -> groups.find { it.id == groupId.toInt() } }
                        .map { group ->
                            stackBuilder.addNextIntent(
                                    GroupActivity.newIntent(context, group))
                            stackBuilder.addNextIntent(
                                    NotificationsActivity.newIntent(context, group.id))
                            stackBuilder
                        }.onErrorReturn {
                            val newStackBuilder = TaskStackBuilder.create(context)
                            newStackBuilder.addNextIntent(MainActivity.newIntent(context))
                            newStackBuilder
                        }
            }

            return Observable.just(stackBuilder)
        }
    }
}
