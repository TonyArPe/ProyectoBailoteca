package com.example.loginycardview.di

import com.example.loginycardview.domain.EventRepository
import com.example.loginycardview.domain.ProfessorRepository
import com.example.loginycardview.domain.VideoRepository
import com.example.loginycardview.domain.usecases.GetEventsUseCase
import com.example.loginycardview.domain.usecases.GetProfessorsUseCase
import com.example.loginycardview.domain.usecases.GetVideosUseCase
import com.example.loginycardview.domain.usecases.SaveEventUseCase
import com.example.loginycardview.domain.usecases.SaveProfessorUseCase
import com.example.loginycardview.domain.usecases.SaveVideoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetEventsUseCase(eventRepository: EventRepository): GetEventsUseCase {
        return GetEventsUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideSaveEventUseCase(eventRepository: EventRepository): SaveEventUseCase {
        return SaveEventUseCase(eventRepository)
    }

    @Provides
    @Singleton
    fun provideGetProfessorsUseCase(professorRepository: ProfessorRepository): GetProfessorsUseCase {
        return GetProfessorsUseCase(professorRepository)
    }

    @Provides
    @Singleton
    fun provideSaveProfessorUseCase(professorRepository: ProfessorRepository): SaveProfessorUseCase {
        return SaveProfessorUseCase(professorRepository)
    }

    @Provides
    @Singleton
    fun provideGetVideosUseCase(videoRepository: VideoRepository): GetVideosUseCase {
        return GetVideosUseCase(videoRepository)
    }

    @Provides
    @Singleton
    fun provideSaveVideoUseCase(videoRepository: VideoRepository): SaveVideoUseCase {
        return SaveVideoUseCase(videoRepository)
    }
}
