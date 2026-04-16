package com.example.chatapplication.apis

import android.util.Log
import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(val sessionManager:  SessionManager,val api: RefreshApi): Interceptor{
    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {


        val token = sessionManager.getAccessToken()
        runBlocking {
            refreshIfNeededSafe(token)
        }
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                Log.e("AuthInterceptor", "TOKEN FOUND ")
                header("Authorization", "Bearer $token")
            }else{
                Log.e("AuthInterceptor", "NO TOKEN FOUND ")
            }
        }.build()
        return chain.proceed(request)

    }
    private suspend fun refreshIfNeededSafe(token: String?) {
        if (token == null) return

        if (shouldRefresh(token)) {
            mutex.withLock {
                val latestToken = sessionManager.getAccessToken()
                if (latestToken != null && shouldRefresh(latestToken)) {
                    val refreshToken = sessionManager.getRefreshToken() ?: return

                    val response = api.refreshToken(Info(refreshToken))
                    val uid = sessionManager.getUid() ?: return
                    sessionManager.saveSession(
                        uid = uid,
                        accessToken = response.token,
                        refreshToken = response.refreshToken
                    )
                }
            }
        }
    }

        private  fun getExpiryTime(token: String): Long {
        val parts = token.split(".")
            if (parts.size < 2) return 0L
        val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
        val json = JSONObject(payload)
        return json.getLong("exp") // seconds
    }
    private fun shouldRefresh(token: String): Boolean {
        val exp = getExpiryTime(token)
        val currentTime = System.currentTimeMillis() / 1000

        val fiveMinutes = 5 * 60

        return currentTime >= (exp - fiveMinutes)
    }
}