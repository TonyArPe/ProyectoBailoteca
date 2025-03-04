package com.example.loginycardview.domain.usecases

import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.EventRepository
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        eventRepository.saveEvent(event)
    }
}
