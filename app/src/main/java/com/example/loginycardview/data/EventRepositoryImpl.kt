package com.example.loginycardview.data

import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.EventRepository

class EventRepositoryImpl : EventRepository {
    override suspend fun getEvents(): List<Event> {
        // Aquí iría la lógica para obtener los eventos desde una API o base de datos
        return emptyList()
    }

    override suspend fun saveEvent(event: Event) {
        // Aquí iría la lógica para guardar un evento en la base de datos
    }
}