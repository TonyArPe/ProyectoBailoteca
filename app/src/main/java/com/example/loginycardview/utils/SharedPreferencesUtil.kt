package com.example.loginycardview.utils

import android.content.SharedPreferences
import android.content.Context

object SharedPreferencesUtil {

    private const val PREFS_NAME = "app_preferences"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveProfileImage(context: Context, imageUri: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("profile_image", imageUri)
        editor.apply()
    }

    fun getProfileImage(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("profile_image", null)
    }

    fun setGuestMode(context: Context, isGuest: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_guest", isGuest)
        editor.apply()
    }

    fun isGuestMode(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean("is_guest", false)
    }
}
