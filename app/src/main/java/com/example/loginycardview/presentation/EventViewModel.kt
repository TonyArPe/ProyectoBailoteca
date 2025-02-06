package com.example.loginycardview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.GetEventsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val getEventsUseCase: GetEventsUseCase) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    fun loadEvents() {
        viewModelScope.launch {
            _events.value = getEventsUseCase()
        }
    }
}