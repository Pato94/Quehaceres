package dadm.frba.utn.edu.ar.quehaceres.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import dadm.frba.utn.edu.ar.quehaceres.ParseDeepLinkActivity
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val services by lazy { Services(this) }
    private var deeplink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deeplink = intent.extras?.getString(ARG_DEEP_LINK)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener { register() }
    }

    @SuppressLint("CheckResult")
    private fun register() {
        services.createUser(email.text.toString(), password.text.toString(), full_name.text.toString())
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    register_form.visibility = View.GONE
                }
                .subscribe(
                        { startApp() },
                        { it.printStackTrace() }
                )
    }

    @SuppressLint("CheckResult")
    private fun startApp() {
        ParseDeepLinkActivity.routeUser(this, deeplink)
                .subscribe { it.startActivities() }
    }

    companion object {
        const val ARG_DEEP_LINK = "deep-link"

        fun newIntent(context: Context, deeplink: String?): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            deeplink?.let { intent.putExtra(ARG_DEEP_LINK, deeplink) }
            return intent
        }
    }
}



