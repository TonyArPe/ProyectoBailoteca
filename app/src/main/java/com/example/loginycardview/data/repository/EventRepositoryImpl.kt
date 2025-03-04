package com.example.loginycardview.data.repository

import com.example.loginycardview.data.local.EventLocalDataSource
import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.EventRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val localDataSource: EventLocalDataSource
) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        return localDataSource.getEvents()
    }

    override suspend fun saveEvent(event: Event) {
        localDataSource.saveEvent(event)
    }
}
