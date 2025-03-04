package com.example.loginycardview.domain

data class Video(
    val title: String,
    val url: String,  // 🔹 Se asegura que el constructor tenga `url`
    val description: String
)
