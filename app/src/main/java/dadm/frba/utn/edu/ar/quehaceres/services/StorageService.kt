package dadm.frba.utn.edu.ar.quehaceres.services

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import dadm.frba.utn.edu.ar.quehaceres.models.User

class StorageService(val context: Context) {
    companion object {
        const val SHARED_PREFERENCES_KEY = "STORAGE_SERVICE"
        const val LOGGED_USER = "LOGGED_USER"
        const val USER_TOKEN = "USER_TOKEN"
    }

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
    val gson: Gson = Gson()

    fun storeUser(user: User) {
        putObject(LOGGED_USER, user)
    }

    fun getUser(user: User): User? {
        return getObject(LOGGED_USER, User::class.java)
    }

    fun storeUserToken(token: String) {
        putObject(USER_TOKEN, token)
    }

    fun getUserToken(token: String): String? {
        return getObject(USER_TOKEN, String::class.java)
    }

    private fun putObject(key: String, thing: Any) {
        sharedPreferences.edit().putString(key, gson.toJson(thing)).apply()
    }

    private fun <T> getObject(key: String, typeOfThing: Class<T>): T? {
        return try {
            gson.fromJson(sharedPreferences.getString(key, null), typeOfThing)
        } catch (e: Exception) {
            null
        }
    }
}
