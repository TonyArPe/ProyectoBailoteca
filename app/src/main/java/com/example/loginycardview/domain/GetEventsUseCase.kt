package com.example.loginycardview.domain

class GetEventsUseCase(private val eventRepository: EventRepository) {
    suspend operator fun invoke(): List<Event> {
        return eventRepository.getEvents()
    }
}