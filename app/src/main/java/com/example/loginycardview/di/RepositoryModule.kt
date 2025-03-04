package com.example.loginycardview.di

import com.example.loginycardview.data.repository.EventRepositoryImpl
import com.example.loginycardview.domain.EventRepository
import com.example.loginycardview.data.repository.ProfessorRepositoryImpl
import com.example.loginycardview.domain.ProfessorRepository
import com.example.loginycardview.data.repository.VideoRepositoryImpl
import com.example.loginycardview.domain.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindProfessorRepository(
        professorRepositoryImpl: ProfessorRepositoryImpl
    ): ProfessorRepository

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository
}
