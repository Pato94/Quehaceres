package dadm.frba.utn.edu.ar.quehaceres

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class QuehaceresApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}