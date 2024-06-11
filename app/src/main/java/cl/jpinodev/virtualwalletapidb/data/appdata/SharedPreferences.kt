package cl.jpinodev.virtualwalletapidb.data.appdata

import android.content.Context
import android.content.SharedPreferences

object SharedPreferences {
    private const val PREFS_NAME = "my_prefs"
    private const val API_KEY = "ApiKey"

    private fun getSharedPreferences(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    fun saveToken(context: Context, token: String) {
        val editor = getSharedPreferences(context)?.edit()
        editor?.putString(API_KEY, token)
        editor?.apply()
    }

    fun getToken(context: Context): String? {
        return getSharedPreferences(context)?.getString(API_KEY, null)
    }

    fun clearToken(context: Context) {
        val editor = getSharedPreferences(context)?.edit()
        editor?.remove(API_KEY)
        editor?.apply()
    }
}