package com.example.chatapplication.models

data class SignInData(
    val email: String,
    val name: String?=null,
    val idToken: String
)
