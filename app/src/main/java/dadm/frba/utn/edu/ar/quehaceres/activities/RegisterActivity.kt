package dadm.frba.utn.edu.ar.quehaceres.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.ParseDeepLinkActivity
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val services by lazy { Services(this) }
    private var deeplink: String? = null
    private var currentImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deeplink = intent.extras?.getString(ARG_DEEP_LINK)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener { register() }
        image_layout.setOnClickListener { onVerifyClicked() }
        image.setActualImageResource(R.drawable.forever_alone_small)
    }

    @SuppressLint("CheckResult")
    private fun register() {
        services.createUser(
                email.text.toString(),
                password.text.toString(),
                full_name.text.toString(),
                currentImage)
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    register_form.visibility = View.GONE
                }
                .subscribe(
                        { startApp() },
                        {
                            loading.visibility = View.GONE
                            register_form.visibility = View.VISIBLE
                            it.printStackTrace()
                        }
                )
    }

    @SuppressLint("CheckResult")
    private fun startApp() {
        ParseDeepLinkActivity.routeUser(this, deeplink)
                .subscribe { it.startActivities() }
    }

    private fun startVerification() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, GroupActivity.CAPTURE_IMAGE_REQUEST)
            }
        }
    }

    private fun onVerifyClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), GroupActivity.CAMERA_PERMISSION_REQUEST)
        } else {
            startVerification()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == GroupActivity.CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK -> {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val imageBitmap = data!!.extras.get("data") as Bitmap
                confirmImage(imageBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            GroupActivity.CAMERA_PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startVerification()
                } else {
                    Toast.makeText(this, "No tenemos permiso", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun confirmImage(imageBitmap: Bitmap) {
        services.upload(imageBitmap)
                .doOnSubscribe {
                    loading_image.visibility = View.VISIBLE
                    image.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading_image.visibility = View.GONE
                            image.visibility = View.VISIBLE
                            image.setImageURI(it.file)
                            currentImage = it.file
                        },
                        {
                            loading_image.visibility = View.GONE
                            image.visibility = View.VISIBLE
                            Toast.makeText(this, "Error while uploading an image", Toast.LENGTH_SHORT).show()
                        }
                )
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



