package dadm.frba.utn.edu.ar.quehaceres.services

import dadm.frba.utn.edu.ar.quehaceres.models.Group
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GroupsService(val apiService: ApiService = ApiService()) {

    fun getGroups(): Observable<List<Group>> {
        return apiService.getGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
