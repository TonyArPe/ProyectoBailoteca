package com.example.loginycardview.domain

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Professor(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl: String? = null,
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("specialty") @set:PropertyName("specialty") var specialty: String = "",
    @get:PropertyName("isTopRated") @set:PropertyName("isTopRated") var isTopRated: Boolean = false,
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("schedule") @set:PropertyName("schedule") var schedule: List<ScheduleItem>? = null,  // 🔹 NUEVO: Lista de horarios
    @get:PropertyName("videoUrl") @set:PropertyName("videoUrl") var videoUrl: String? = null // 🔹 NUEVO: URL de video
) : Parcelable

@Parcelize
data class ScheduleItem(
    @get:PropertyName("day") @set:PropertyName("day") var day: String = "",
    @get:PropertyName("time") @set:PropertyName("time") var time: String = "",
    @get:PropertyName("room") @set:PropertyName("room") var room: String = ""
) : Parcelable
