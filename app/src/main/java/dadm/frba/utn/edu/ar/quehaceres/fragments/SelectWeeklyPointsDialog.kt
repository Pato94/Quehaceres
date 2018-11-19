package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.SeekBar
import dadm.frba.utn.edu.ar.quehaceres.R
import kotlinx.android.synthetic.main.dialog_select_weekly_points.*

class SelectWeeklyPointsDialog(
        context: Context,
        val currentPoints: Int,
        val onPointsSelected: (Int) -> Unit
) : AlertDialog.Builder(context) {

    override fun show(): AlertDialog {
        setTitle("Puntos semanales")
        setView(R.layout.dialog_select_weekly_points)

        var callback: () -> Unit = {}

        setPositiveButton("Confirmar") { _, _ -> callback() }
        setNegativeButton("Cancelar") { _, _ -> }

        val dialog = super.show()

        var points = currentPoints
        val updateText = {
            dialog.weekly_points.text = "Actual: $points"
        }
        updateText()

        dialog.seek_bar.progress = currentPoints
        dialog.seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                points = progress
                updateText()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        callback = {
            onPointsSelected(dialog.seek_bar.progress)
        }

        return dialog
    }
}