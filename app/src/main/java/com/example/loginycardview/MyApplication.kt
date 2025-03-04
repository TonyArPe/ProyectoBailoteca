package com.example.loginycardview

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Aquí puedes inicializar otros servicios si es necesario
    }
}
