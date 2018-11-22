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
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.*

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

    fun users(userId: Int): Observable<List<User>> = api.users(userId).map { remoteUsers -> remoteUsers.map { User(it) } }

    fun createGroup(currentId: Int, name: String, usersAndPoints: List<Pair<User, Int>>) =
            api.createGroup(currentId, CreateGroupRequest(name, usersAndPoints.map { UserAndPoints(it.first.id, it.second) }))

    fun availableTasks(userId: Int, groupId: Int): Observable<List<Task>> = api.availableTasks(userId, groupId)

    fun myTasks(userId: Int, groupId: Int): Observable<List<Task>> = api.myTasks(userId, groupId)

    fun assignTask(userId: Int, groupId: Int, taskId: Int) = api.assignTask(userId, groupId, taskId)

    interface Api {
        @POST("login")
        fun login(@Body body: LoginRequest): Observable<LoginResponse>

        @GET("mygroups")
        fun myGroups(@Header("X-UserId") userId: Int): Observable<List<Group>>

        @GET("users")
        fun users(@Header("X-UserId") userId: Int): Observable<List<LoginResponse>>

        @POST("groups")
        fun createGroup(@Header("X-UserId") userId: Int, @Body createGroupRequest: CreateGroupRequest): Observable<ResponseBody>

        @GET("groups/{group_id}/available_tasks")
        fun availableTasks(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<List<Task>>

        @GET("groups/{group_id}/my_tasks")
        fun myTasks(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<List<Task>>

        @POST("groups/{group_id}/assign_task/{task_id}")
        fun assignTask(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int, @Path("task_id") taskId: Int): Observable<ResponseBody>
    }

    data class LoginRequest(val username: String, val password: String)

    data class LoginResponse(val id: Int, val username: String, val password: String, val fullName: String)

    data class CreateGroupRequest(val name: String, val members: List<UserAndPoints>)

    @Parcelize
    data class UserAndPoints(val id: Int, val points: Int): Parcelable

    @Parcelize
    data class Group(val id: Int, val name: String, val members: List<UserAndPoints>, val tasks: List<MemberTasks>?) : Parcelable

    @Parcelize
    data class MemberTasks(val member: Int, val assigned: List<Int>) : Parcelable

    @Parcelize
    data class Task(val id: Int, val name: String): Parcelable
}