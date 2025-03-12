package com.example.loginycardview.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginycardview.domain.Event
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()  // 🔹 Definimos MutableLiveData
    val events: LiveData<List<Event>> get() = _events  // 🔹 LiveData para exponer los eventos

    init {
        loadEvents()
    }

    fun loadEvents() {
        firestore.collection("events").get()
            .addOnSuccessListener { result ->
                val eventList = result.documents.mapNotNull { it.toObject(Event::class.java) }
                _events.value = eventList
                Log.d("EventViewModel", "Eventos cargados correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("EventViewModel", "Error al cargar eventos", exception)
            }
    }

    fun addEvent(event: Event) {
        val newDocRef = firestore.collection("events").document()
        event.id = newDocRef.id
        newDocRef.set(event)
            .addOnSuccessListener {
                loadEvents()
                Log.d("EventViewModel", "Evento añadido correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("EventViewModel", "Error al añadir evento", exception)
            }
    }

    fun updateEvent(event: Event) {
        val eventRef = firestore.collection("events").document(event.id)

        val updates = mapOf(
            "title" to event.title,
            "description" to event.description,
            "date" to event.date,
            "location" to event.location
        )

        eventRef.update(updates)
            .addOnSuccessListener {
                loadEvents()
                Log.d("EventViewModel", "Evento actualizado correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("EventViewModel", "Error al actualizar evento", exception)
            }
    }

    fun deleteEvent(eventId: String) {
        firestore.collection("events").document(eventId).delete()
            .addOnSuccessListener {
                loadEvents()
                Log.d("EventViewModel", "Evento eliminado correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("EventViewModel", "Error al eliminar evento", exception)
            }
    }
}
