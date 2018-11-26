package dadm.frba.utn.edu.ar.quehaceres.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.facebook.drawee.drawable.ProgressBarDrawable
import dadm.frba.utn.edu.ar.quehaceres.R

class ImageLoadProgressBar : ProgressBarDrawable() {
    internal var level: Float = 0f
    internal var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    internal var color = R.color.accent
    internal val oval = RectF()
    internal var radius = 10f

    init {
        paint.setStrokeWidth(10f)
        paint.setStyle(Paint.Style.STROKE)
    }

    override fun onLevelChange(level: Int): Boolean {
        this.level = level.toFloat()
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
//        val width = bounds.width()
//        val height = bounds.height()

        oval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius)

        drawCircle(canvas, level, color)
    }


    private fun drawCircle(canvas: Canvas, level: Float, color: Int) {
        paint.color = color
        var angle: Float = 360 / 1f
        angle *= level
        canvas.drawArc(oval, 0f, angle, false, paint)
    }
}