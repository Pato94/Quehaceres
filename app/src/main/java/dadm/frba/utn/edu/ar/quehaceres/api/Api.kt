package dadm.frba.utn.edu.ar.quehaceres.api

import android.os.Parcelable
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dadm.frba.utn.edu.ar.quehaceres.models.User
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Header


class Api {
    var api: Api

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://que-haceres-api.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        api = retrofit.create(Api::class.java)
    }

    fun login(username: String, password: String): Observable<User> =
            api.login(LoginRequest(username, password)).map { User(it) }

    fun myGroups(userId: Int): Observable<List<Group>> = api.myGroups(userId)

    fun users(): Observable<List<User>> = api.users().map { remoteUsers -> remoteUsers.map { User(it) } }

    interface Api {
        @POST("login")
        fun login(@Body body: LoginRequest): Observable<LoginResponse>

        @GET("mygroups")
        fun myGroups(@Header("X-UserId") userId: Int): Observable<List<Group>>

        @GET("users")
        fun users(): Observable<List<LoginResponse>>
    }

    data class LoginRequest(val username: String, val password: String)

    data class LoginResponse(val id: Int, val username: String, val password: String, val fullName: String)

    @Parcelize
    data class Group(val id: Int, val name: String, val members: List<Int>, val tasks: List<Task>) : Parcelable

    @Parcelize
    data class Task(val member: Int, val assigned: List<Int>) : Parcelable
}