package com.example.loginycardview.data.local

import com.example.loginycardview.domain.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventLocalDataSource @Inject constructor() {
    private val eventList = mutableListOf<Event>()

    suspend fun getEvents(): List<Event> {
        return eventList
    }

    suspend fun saveEvent(event: Event) {
        eventList.add(event)
    }
}
