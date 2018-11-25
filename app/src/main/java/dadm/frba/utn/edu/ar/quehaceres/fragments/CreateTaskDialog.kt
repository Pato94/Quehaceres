package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.SeekBar
import dadm.frba.utn.edu.ar.quehaceres.R
import kotlinx.android.synthetic.main.dialog_create_task.*

class CreateTaskDialog(
        context: Context,
        val maxPoints: Int,
        val onTaskConfirmed: (String, Int) -> Unit
) : AlertDialog.Builder(context) {

    override fun show(): AlertDialog {
        setTitle("Nueva Tarea")
        setView(R.layout.dialog_create_task)

        var callback: () -> Unit = {}

        setPositiveButton("Confirmar") { _, _ -> callback() }
        setNegativeButton("Cancelar") { _, _ -> }

        val dialog = super.show()

        var points = 0
        val updateText = {
            dialog.actual_reward.text = "Recompensa Actual: ${points + 100}"
            if (points > 0) {
                dialog.warning_layout.visibility = View.VISIBLE
                dialog.warning.text = "Crear esta tarea descontará $points puntos de tu balance"
            } else {
                dialog.warning_layout.visibility = View.GONE
            }
        }
        updateText()

        dialog.seek_bar.progress = 0
        dialog.seek_bar.max = maxPoints - 100
        dialog.seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                points = progress
                updateText()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        callback = {
            onTaskConfirmed(dialog.name.text.toString(), dialog.seek_bar.progress + 100)
        }

        return dialog
    }
}