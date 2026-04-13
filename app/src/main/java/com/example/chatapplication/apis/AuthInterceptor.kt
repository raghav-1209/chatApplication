package com.example.chatapplication.apis

import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(val sessionManager:  SessionManager): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = sessionManager.getAccessToken()
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)

    }
}