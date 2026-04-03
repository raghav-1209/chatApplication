package com.example.chatapplication.models
data class WholeUser(
    val credentials: UserData,
    val image: String?=null,
    val bio: String?=null
)
