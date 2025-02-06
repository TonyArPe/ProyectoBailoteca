package com.example.loginycardview.domain

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun saveEvent(event: Event)
}