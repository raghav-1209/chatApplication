package com.example.chatapplication.models

data class UserSession(
    val refreshToken: String,
    val token: String,
)
