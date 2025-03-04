package com.example.loginycardview.data.repository

import android.util.Log
import com.example.loginycardview.domain.Event
import com.example.loginycardview.domain.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        return try {
            val snapshot = firestore.collection("events").get().await()
            snapshot.documents.mapNotNull { it.toObject(Event::class.java) }
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al obtener eventos: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun saveEvent(event: Event) {
        try {
            firestore.collection("events").add(event).await()
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al guardar evento: ${e.message}", e)
        }
    }
}
