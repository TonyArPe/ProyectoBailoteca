package com.example.loginycardview.data

import android.os.Parcel
import android.os.Parcelable

data class Professor(
    val imageResId: Int,
    val name: String,
    val specialty: String,
    val isTopRated: Boolean,
    val description: String,
    val email: String
) : Parcelable {

    // Aquí creamos el constructor que necesita Parcel
    constructor(parcel: Parcel) : this(
        imageResId = parcel.readInt(),
        name = parcel.readString() ?: "",
        specialty = parcel.readString() ?: "",
        isTopRated = parcel.readByte() != 0.toByte(),
        description = parcel.readString() ?: "",
        email = parcel.readString() ?: ""
    )

    // Este método escribe los datos en el Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageResId)
        parcel.writeString(name)
        parcel.writeString(specialty)
        parcel.writeByte(if (isTopRated) 1 else 0)
        parcel.writeString(description)
        parcel.writeString(email)
    }

    // Este método devuelve el descriptor de objetos Parcelable
    override fun describeContents(): Int = 0

    // Este es el objeto Companion necesario para el Parcelable
    companion object CREATOR : Parcelable.Creator<Professor> {
        override fun createFromParcel(parcel: Parcel): Professor {
            return Professor(parcel)
        }

        override fun newArray(size: Int): Array<Professor?> {
            return arrayOfNulls(size)
        }
    }
}
