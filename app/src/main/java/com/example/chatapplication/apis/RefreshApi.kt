package com.example.chatapplication.apis

import com.example.chatapplication.models.UserSession
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RefreshApi {
    @POST("/auth/refreshToken")
    suspend fun refreshToken(@Body info: Info): UserSession
}