package com.example.chatapplication.models

import kotlinx.serialization.Serializable

data class StatusWithUser(
    val userId: String,
    val name: String,
    val profileImage: String?,
    val statusImage: String,
    val createdAt: Long
)
