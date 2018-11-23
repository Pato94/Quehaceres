package dadm.frba.utn.edu.ar.quehaceres.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val services by lazy { Services(this) }

    private fun goToLoginActivity() {
        startActivity(LoginActivity.newIntent(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener { register() }
    }

    @SuppressLint("CheckResult")
    private fun register() {
        services.createUser(email.text.toString(), password.text.toString(), full_name.text.toString())
                .subscribe(
                        { goToLoginActivity() },
                        { it.printStackTrace() }
                )
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}



