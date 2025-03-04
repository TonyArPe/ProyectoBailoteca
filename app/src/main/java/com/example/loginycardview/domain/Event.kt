package com.example.loginycardview.domain

import com.google.firebase.firestore.PropertyName

data class Event(
    @get:PropertyName("title") @set:PropertyName("title") var title: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = ""
) {
    constructor() : this("", "", "")
}
