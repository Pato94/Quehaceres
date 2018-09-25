package dadm.frba.utn.edu.ar.quehaceres.services

import dadm.frba.utn.edu.ar.quehaceres.models.Group
import dadm.frba.utn.edu.ar.quehaceres.models.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ApiService {

    // TODO: Replace with actual api calls

    fun loginUser(email: String, password: String): Observable<LoginResponse> {
        return Observable
                .just(LoginResponse(email))
                .delay(500, TimeUnit.MILLISECONDS)
    }

    fun getGroups(): Observable<List<Group>> {
        return Observable.just(
                listOf(
                        Group("Familia", "Gabo ganó 60 puntos por lavar los platos", emptyList()),
                        Group("DADM", "Patricio ganó 30 puntos por hacer los primeros commits", emptyList()),
                        Group("Proyecto", "Todavía nadie hizo nada en Proyecto!", emptyList())
                )
        ).delay(500, TimeUnit.MILLISECONDS)
    }

    class LoginResponse(val token: String)
}
