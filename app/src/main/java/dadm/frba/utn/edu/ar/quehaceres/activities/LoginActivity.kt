package dadm.frba.utn.edu.ar.quehaceres.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.ParseDeepLinkActivity
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private val services by lazy { Services(this) }
    private var deeplink: String? = null

    companion object {
        const val ARG_DEEPLINK = "deeplink"

        fun newIntent(context: Context, deeplink: String?): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            deeplink?.let { intent.putExtra(ARG_DEEPLINK, it) }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deeplink = intent.extras?.getString(ARG_DEEPLINK)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }

        register_button.setOnClickListener { goToRegisterActivity() }
    }

    @SuppressLint("CheckResult")
    private fun attemptLogin() {
      services.login(email.text.toString(), password.text.toString())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe { showProgress(true) }
          .subscribe(
                  {
                      showProgress(false)
                      startApp()
                  },
                  {
                      showProgress(false)
                      Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                  }
          )
    }

    @SuppressLint("CheckResult")
    private fun startApp() {
        ParseDeepLinkActivity.routeUser(this, deeplink)
                .subscribe {
                    it.startActivities()
                }
    }

    private fun goToRegisterActivity() {
        startActivity(RegisterActivity.newIntent(this, deeplink))
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }
}
