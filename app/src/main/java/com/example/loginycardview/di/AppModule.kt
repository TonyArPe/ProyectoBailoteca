package com.example.loginycardview.di

import android.content.Context
import androidx.room.Room
import com.example.loginycardview.data.local.dao.ProfessorDao
import com.example.loginycardview.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 🔹 Solución: Proporcionamos `Context` para inyectarlo en otros módulos
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideProfessorDao(database: AppDatabase): ProfessorDao {
        return database.professorDao()
    }
}
