package dadm.frba.utn.edu.ar.quehaceres.activities

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

open class BaseActivity: AppCompatActivity() {
    var compositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }
}