package com.tallbreadstick.android

import android.content.Context
import android.content.SharedPreferences

object AuthPreferences {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        prefs(context).edit().putString(KEY_TOKEN, token).apply()
        inMemoryToken = token
    }

    fun clearToken(context: Context) {
        prefs(context).edit().remove(KEY_TOKEN).apply()
        inMemoryToken = null
    }

    fun getToken(): String? {
        // Called from interceptor where Context isn't available; read via a stored value if needed.
        // For simplicity keep a volatile in-memory fallback populated by activities.
        return inMemoryToken
    }

    @Volatile
    var inMemoryToken: String? = null

    fun loadToken(context: Context): String? {
        val t = prefs(context).getString(KEY_TOKEN, null)
        inMemoryToken = t
        return t
    }
}
