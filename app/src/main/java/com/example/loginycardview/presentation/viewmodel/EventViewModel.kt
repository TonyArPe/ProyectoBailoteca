package com.example.loginycardview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.usecases.GetEventsUseCase
import com.example.loginycardview.domain.usecases.SaveEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val saveEventUseCase: SaveEventUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    fun loadEvents() {
        viewModelScope.launch {
            _events.value = getEventsUseCase()
        }
    }

    fun saveEvent(event: Event) {
        viewModelScope.launch {
            saveEventUseCase(event)
            loadEvents() // Recargar eventos después de guardar uno nuevo
        }
    }
}
