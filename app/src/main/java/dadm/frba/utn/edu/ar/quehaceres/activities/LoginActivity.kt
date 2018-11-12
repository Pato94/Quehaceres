package dadm.frba.utn.edu.ar.quehaceres.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.services.StorageService
import dadm.frba.utn.edu.ar.quehaceres.services.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    val userService by lazy { UserService(StorageService(this)) }
    val api by lazy { Api().api }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
      val result = api.login(Api.LoginRequest(email.text.toString(), password.text.toString()))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe { showProgress(true) }
          .subscribe(
                  {
                      showProgress(false)
                      goToMainActivity()
                  },
                  {
                      showProgress(false)
                      Toast.makeText(this@LoginActivity, "ERROR", Toast.LENGTH_SHORT).show()
                  }
          )
//        userService
//                .loginUser(email.text.toString(), password.text.toString())
//                .doOnSubscribe { showProgress(true) }
//                .subscribe(
//                        {
//                            showProgress(false)
//                            goToMainActivity()
//                        },
//                        {
//                            showProgress(false)
//                            Toast.makeText(this@LoginActivity, "ERROR", Toast.LENGTH_SHORT).show()
//                        }
//                )
    }

    private fun goToMainActivity() {
        startActivity(MainActivity.newIntent(this))
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
