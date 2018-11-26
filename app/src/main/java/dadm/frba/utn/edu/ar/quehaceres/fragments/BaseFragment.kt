package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {
    var compositeSubscription: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        compositeSubscription = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        compositeSubscription.dispose()
    }
}