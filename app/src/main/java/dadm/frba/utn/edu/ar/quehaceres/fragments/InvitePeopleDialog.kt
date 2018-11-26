package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v4.app.ShareCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.synthetic.main.dialog_invite_people.*

class InvitePeopleDialog(
        val activity: Activity,
        val groupId: Int
) : AlertDialog.Builder(activity) {

    override fun show(): AlertDialog {
        setTitle("Invitar al grupo")
        setView(R.layout.dialog_invite_people)

        val dialog = super.show()

        dialog.link.setText("${Api.BASE_URL}ginvite/$groupId")
        dialog.copy.setOnClickListener {
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Share link", dialog.link.text.toString())
            clipboard.primaryClip = clip
            Toast.makeText(activity, "Link copiado al portapapeles", Toast.LENGTH_SHORT).show()
        }

        dialog.share.setOnClickListener {
            ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setChooserTitle("Invitar al grupo")
                    .setText(dialog.link.text.toString())
                    .startChooser()
        }

        return dialog
    }
}