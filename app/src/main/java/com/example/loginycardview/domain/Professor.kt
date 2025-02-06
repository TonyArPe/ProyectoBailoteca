package com.example.loginycardview.domain

data class Professor(
    val imageResId: Int,
    val name: String,
    val specialty: String,
    val isTopRated: Boolean,
    val description: String,
    val email: String
)