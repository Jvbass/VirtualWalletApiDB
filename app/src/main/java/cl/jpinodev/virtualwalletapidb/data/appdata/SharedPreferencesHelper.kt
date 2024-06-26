package cl.jpinodev.virtualwalletapidb.data.appdata

import android.content.Context
import android.content.SharedPreferences
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import com.google.gson.Gson

object SharedPreferencesHelper {
    private const val PREFS_NAME = "my_prefs" // identificador para las sharedpreferencens
    private const val TOKEN = "ApiKey"
    private const val CONNECTED_USER = "User"
    private const val CONNECTED_ACCOUNT = "Account"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString(TOKEN, null)
    }

    fun saveConnectedUser(context: Context, user: Users) {
        val editor = getSharedPreferences(context).edit()
        val userJson = Gson().toJson(user)
        editor.putString(CONNECTED_USER, userJson)
        editor.apply()
    }

    fun getConnectedUser(context: Context): Users? {
        val userJson = getSharedPreferences(context).getString(CONNECTED_USER, null)
        return if (userJson != null) {
            Gson().fromJson(userJson, Users::class.java)
        } else {
            null
        }
    }

    fun saveAccount(context: Context, account: Accounts) {
        val editor = getSharedPreferences(context).edit()
        val json = Gson().toJson(account)
        editor.putString(CONNECTED_ACCOUNT, json)
        editor.apply()
    }

    fun getAccount(context: Context): Accounts? {
        val json = getSharedPreferences(context).getString(CONNECTED_ACCOUNT, null)
        return if (json != null) {
            Gson().fromJson(json, Accounts::class.java)
        } else {
            null
        }
    }

    fun clearAll(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(CONNECTED_USER)
        editor.remove(CONNECTED_ACCOUNT)
        editor.remove(TOKEN)
        editor.apply()
    }
}