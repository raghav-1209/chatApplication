package com.example.chatapplication.prefernces

import android.content.Context
import javax.inject.Inject

class SessionManager @Inject constructor(context: Context) {

    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveSession(
        uid: String,
        accessToken: String,
        refreshToken: String
    ) {
        prefs.edit().apply {
            putString("uid", uid)
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
            commit()
        }
    }

    fun getUid(): String? = prefs.getString("uid", null)

    fun getAccessToken(): String? = prefs.getString("access_token", null)

    fun getRefreshToken(): String? = prefs.getString("refresh_token", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}