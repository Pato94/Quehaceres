package dadm.frba.utn.edu.ar.quehaceres.services

import dadm.frba.utn.edu.ar.quehaceres.models.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserService(private val storageService: StorageService, private val apiService: ApiService = ApiService()) {

    fun isEmailValid(email: String) = email.contains("@")

    fun isPasswordSecureEnough(password: String) = password.length > 5

    fun loginUser(email: String, password: String): Observable<Any> {
        if (!isEmailValid(email)) {
            return Observable.error(IllegalArgumentException("Email is not valid"))
        }

        if (!isPasswordSecureEnough(password)) {
            return Observable.error(IllegalArgumentException("Password is too short"))
        }

        return apiService.loginUser(email, password)
                .doOnNext {
                    storageService.storeUser(User(email))
                    storageService.storeUserToken(it.token)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { Any() }
    }
}
