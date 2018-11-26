package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.app.Activity
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AlertDialog
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.synthetic.main.dialog_validate_task.*

class ValidateTaskDialog(
        val activity: Activity,
        val notification: Api.Notification,
        val onValidateTask: (Api.Notification) -> Unit
) : AlertDialog.Builder(activity) {

    override fun show(): AlertDialog {
        setTitle(notification.message)
        setView(R.layout.dialog_validate_task)

        setPositiveButton("Validar") { _, _ -> onValidateTask(notification) }
        setNegativeButton("Cancelar") { _, _ -> }

        val dialog = super.show()

        dialog.image.hierarchy.setProgressBarImage(CircularProgressDrawable(activity))
        dialog.image.setImageURI(notification.url)

        return dialog
    }
}