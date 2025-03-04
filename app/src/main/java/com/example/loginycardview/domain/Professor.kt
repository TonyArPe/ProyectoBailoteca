package com.example.loginycardview.domain

import com.google.firebase.firestore.PropertyName

data class Professor(
    @get:PropertyName("imageResId") @set:PropertyName("imageResId") var imageResId: Int = 0,
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("specialty") @set:PropertyName("specialty") var specialty: String = "",
    @get:PropertyName("isTopRated") @set:PropertyName("isTopRated") var isTopRated: Boolean = false,
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = ""
) {
    constructor() : this(0, "", "", false, "", "")
}
