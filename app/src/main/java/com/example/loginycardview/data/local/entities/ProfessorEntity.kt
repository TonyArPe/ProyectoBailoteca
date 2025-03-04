package com.example.loginycardview.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "professors")
data class ProfessorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageResId: Int,
    val name: String,
    val specialty: String,
    val isTopRated: Boolean,
    val description: String,
    val email: String
)
