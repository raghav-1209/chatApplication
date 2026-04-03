package com.example.chatapplication.models

data class ImgBBResponse(
    val data: ImgBBData,
    val success: Boolean,
    val status: Int
)
data class ImgBBData(
    val id: String,
    val url: String,
    val display_url: String,
    val url_viewer: String,
    val delete_url: String?
)
