package com.example.chatapplication.apis

import com.example.chatapplication.models.FcmData
import com.example.chatapplication.models.SignInData
import com.example.chatapplication.models.UserSession
import com.example.chatapplication.models.loginData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DataBaseApis {
    @POST("/auth/fcmToken")
    suspend fun saveFcm(@Body fcmData: FcmData):response
    @POST("/auth/retry")
    suspend fun  signIn(@Body signInData: SignInData): UserSession
    @POST("/auth/refreshToken")
    suspend fun  refreshToken(@Body info: Info): UserSession
    @POST("check")
    suspend fun check():response
}

data class Info(
    val token: String
)
data class response(
    val success: Boolean,
    val text: String
)