package com.example.loginycardview.domain

import com.google.firebase.firestore.PropertyName

data class Video(
    @get:PropertyName("title") @set:PropertyName("title") var title: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("url") @set:PropertyName("url") var url: String = ""
) {
    // 🔴 Constructor sin argumentos requerido por Firestore
    constructor() : this("", "", "")
}

