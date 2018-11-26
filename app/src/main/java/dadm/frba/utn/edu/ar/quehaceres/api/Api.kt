package dadm.frba.utn.edu.ar.quehaceres.api

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dadm.frba.utn.edu.ar.quehaceres.models.User
import io.reactivex.Observable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.*
import java.io.ByteArrayOutputStream

class Api {
    val BASE_URL = "https://que-haceres-api.herokuapp.com/"
    var api: Api

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    fun createGroup(currentId: Int, url: String?, name: String, usersAndPoints: List<Pair<User, Int>>) =
            api.createGroup(currentId, CreateGroupRequest(url, name, usersAndPoints.map { UserAndPoints(it.first.id, it.second) }))

    fun availableTasks(userId: Int, groupId: Int): Observable<List<Task>> = api.availableTasks(userId, groupId).map { list -> list.map { it.toTask() } }

    fun myTasks(userId: Int, groupId: Int): Observable<List<Task>> = api.myTasks(userId, groupId).map { list -> list.map { it.toTask() } }

    fun createUser(username: String, password: String, full_name: String, currentImage: String?): Observable<User> =
            api.createUser(CreateUserRequest(username, password, full_name, currentImage)).map { User(it) }

    fun assignTask(userId: Int, groupId: Int, taskId: Int) = api.assignTask(userId, groupId, taskId)

    fun uploadBitmap(bitmap: Bitmap): Observable<UploadResponse> {
        val body = RequestBody.create(MediaType.parse("multipart/form-data"), bytesFromBitmap(bitmap))
        val part = MultipartBody.Part.createFormData("image", "image.jpg", body)
        return api.upload(part)
                .map { UploadResponse("$BASE_URL${it.file}")}
    }

    fun verifyTask(userId: Int, groupId: Int, taskId: Int, photoUrl: String) =
            api.verifyTask(userId, groupId, taskId, VerificationRequest(photoUrl))

    fun createTask(userId: Int, groupId: Int, name: String, reward: Int) =
            api.createTask(userId, groupId, CreateTaskRequest(name, reward))

    fun postToken(userId: Int, token: String): Observable<ResponseBody> =
            api.postToken(userId, token)

    fun deleteToken(userId: Int): Observable<ResponseBody> =
            api.deleteToken(userId)

    fun getGroupNotifications(userId: Int, groupId: Int) = api.getGroupNotifications(userId, groupId)

    fun addToGroup(userId: Int, groupId: Int) = api.addToGroup(userId, groupId)

    fun validateTask(userId: Int, groupId: Int, taskId: Int) = api.validateTask(userId, groupId, taskId)

    private fun bytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
        return stream.toByteArray()
    }

    interface Api {
        @POST("login")
        fun login(@Body body: LoginRequest): Observable<RemoteUser>

        @GET("mygroups")
        fun myGroups(@Header("X-UserId") userId: Int): Observable<List<Group>>

        @GET("users")
        fun users(@Header("X-UserId") userId: Int): Observable<List<RemoteUser>>

        @POST("groups")
        fun createGroup(@Header("X-UserId") userId: Int, @Body createGroupRequest: CreateGroupRequest): Observable<ResponseBody>

        @GET("groups/{group_id}/available_tasks")
        fun availableTasks(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<List<RemoteTask>>

        @GET("groups/{group_id}/my_tasks")
        fun myTasks(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<List<RemoteTask>>

        @POST("users")
        fun createUser(@Body user: CreateUserRequest): Observable<RemoteUser>

        @POST("groups/{group_id}/assign_task/{task_id}")
        fun assignTask(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int, @Path("task_id") taskId: Int): Observable<ResponseBody>

        @POST("groups/{group_id}/verify_task/{task_id}")
        fun verifyTask(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int, @Path("task_id") taskId: Int, @Body verificationRequest: VerificationRequest): Observable<ResponseBody>

        @POST("groups/{group_id}/task")
        fun createTask(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int, @Body request: CreateTaskRequest): Observable<ResponseBody>

        @POST("token")
        fun postToken(@Header("X-UserId") userId: Int, @Query("value") token: String): Observable<ResponseBody>

        @DELETE("token")
        fun deleteToken(@Header("X-UserId") userId: Int): Observable<ResponseBody>

        @GET("groups/{group_id}/notifications")
        fun getGroupNotifications(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<List<Notification>>

        @POST("groups/{group_id}/subscribe")
        fun addToGroup(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int): Observable<Group>

        @POST("groups/{group_id}/validate/{task_id}")
        fun validateTask(@Header("X-UserId") userId: Int, @Path("group_id") groupId: Int, @Path("task_id") taskId: Int): Observable<ResponseBody>

        @Multipart
        @POST("upload")
        fun upload(@Part file: MultipartBody.Part): Observable<UploadResponse>
    }

    data class LoginRequest(val username: String, val password: String)

    @Parcelize
    data class RemoteUser(val id: Int, val username: String, val password: String, val fullName: String, val photoUrl: String): Parcelable

    data class CreateGroupRequest(val url: String?, val name: String, val members: List<UserAndPoints>)

    data class RemoteTask(val id: Int, val name: String, val reward: Int, val createdBy: RemoteUser, val status: String?) {
        fun toTask() = Task(id, name, reward, User(createdBy), status)
    }

    @Parcelize
    data class UserAndPoints(val id: Int, val points: Int): Parcelable

    @Parcelize
    data class Member(val id: Int, val user: RemoteUser, val points: Int): Parcelable {
        @IgnoredOnParcel
        val actualUser = User(user)
    }

    @Parcelize
    data class Group(val id: Int, val url: String, val name: String, val lastMessage: String, val members: List<Member>, val tasks: List<MemberTasks>?) : Parcelable

    @Parcelize
    data class MemberTasks(val member: Int, val assigned: List<Int>) : Parcelable

    @Parcelize
    data class Task(val id: Int, val name: String, val reward: Int, val createdBy: User, val status: String?): Parcelable

    data class CreateUserRequest(val username: String, val password: String, val fullName: String, val photoUrl: String?)

    data class UploadResponse(val file: String)

    data class VerificationRequest(val photoUrl: String)

    data class CreateTaskRequest(val name: String, val reward: Int)

    data class Notification(
            val producer: RemoteUser,
            val type: String,
            val message: String,
            val taskId: Int,
            val url: String?,
            val status: String?)
}