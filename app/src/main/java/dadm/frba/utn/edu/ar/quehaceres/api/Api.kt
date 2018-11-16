package dadm.frba.utn.edu.ar.quehaceres.api

import android.os.Parcelable
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
  lateinit var retrofit: Retrofit
  lateinit var api: Api

  init {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://que-haceres-api.herokuapp.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    api = retrofit.create(Api::class.java)
  }

  interface Api {
    @POST("login")
    fun login(@Body body: LoginRequest): Observable<LoginResponse>

    @GET("mygroups")
    fun myGroups(@Header("X-UserId") userId: Int): Observable<List<Group>>
  }

  data class LoginRequest(val username: String, val password: String)

  data class LoginResponse(val id: Int, val username: String, val password: String, val fullName: String)

  @Parcelize
  data class Group(val id: Int, val name: String, val members: List<Int>, val tasks: List<Task>): Parcelable

  @Parcelize
  data class Task(val member: Int, val assigned: List<Int>): Parcelable
}