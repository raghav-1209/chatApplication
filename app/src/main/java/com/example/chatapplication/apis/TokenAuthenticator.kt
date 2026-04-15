package com.example.chatapplication.apis

import android.util.Log
import com.example.chatapplication.auth.AuthManager
import com.example.chatapplication.prefernces.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val apis: RefreshApi,
    private val authManager: AuthManager
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {

        // Prevent infinite loop
        if (responseCount(response) >= 2) {
            authManager.logout()
            return null
        }

        val refreshToken = sessionManager.getRefreshToken() ?: return null

        return try {
            val refreshResponse = runBlocking {
                apis.refreshToken(Info(refreshToken))
            }
            Log.e("AUTH", "Refreshing token...")
            Log.e("AUTH", "New token: ${refreshResponse.token}")

            val uid = sessionManager.getUid() ?: return null

            // Save new tokens
            sessionManager.saveSession(
                uid = uid,
                accessToken = refreshResponse.token,
                refreshToken = refreshResponse.refreshToken
            )

            // Retry original request with new token
            response.request.newBuilder()
                .header("Authorization", "Bearer ${refreshResponse.token}")
                .build()

        } catch (e: Exception) {
            authManager.logout()
            null
        }
    }

    private fun responseCount(response: okhttp3.Response): Int {
        var count = 1
        var res = response.priorResponse
        while (res != null) {
            count++
            res = res.priorResponse
        }
        return count
    }
}