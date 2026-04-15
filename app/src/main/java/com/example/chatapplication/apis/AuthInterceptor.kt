package com.example.chatapplication.apis

import android.util.Log
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
                Log.e("AuthInterceptor", "TOKEN FOUND ")
                header("Authorization", "Bearer $token")
            }else{
                Log.e("AuthInterceptor", "NO TOKEN FOUND ")
            }
        }.build()
        return chain.proceed(request)

    }
}