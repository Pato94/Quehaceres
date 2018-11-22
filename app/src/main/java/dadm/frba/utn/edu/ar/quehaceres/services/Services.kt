package dadm.frba.utn.edu.ar.quehaceres.services

import android.content.Context
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.api.Api.Group
import dadm.frba.utn.edu.ar.quehaceres.models.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Services(private val storageService: StorageService, private val api: Api = Api()) {

    constructor(context: Context): this(StorageService(context), Api())

    fun login(email: String, password: String): Observable<Any> {
        return api.login(email, password)
                .doOnNext { storageService.storeUser(it) }
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { Any() }
    }

    fun myGroups(): Observable<List<Group>> {
        return api.myGroups(currentId())
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun allUsers(): Observable<List<User>> {
        return api.users(currentId())
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun createGroup(name: String, membersAndPoints: List<Pair<User, Int>>): Observable<Any> {
        return api.createGroup(currentId(), name, membersAndPoints)
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { Any() }
    }

    fun availableTasks(groupId: Int): Observable<List<Api.Task>> {
        return api.availableTasks(currentId(), groupId)
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun myTasks(groupId: Int): Observable<List<Api.Task>> {
        return api.myTasks(currentId(), groupId)
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun assignTask(groupId: Int, taskId: Int): Observable<Any> {
        return api.assignTask(currentId(), groupId, taskId)
                .doOnError { it.printStackTrace() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { Any() }
    }

    private fun currentId(): Int {
        val currentUser = storageService.getUser() ?: throw IllegalAccessError("No user stored")
        return currentUser.id
    }

    private fun isEmailValid(email: String) = email.contains("@")

    private fun isPasswordSecureEnough(password: String) = password.length > 5

}
