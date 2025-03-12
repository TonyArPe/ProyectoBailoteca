package com.example.loginycardview.data.repository

import android.util.Log
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfessorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProfessorRepository {

    override suspend fun getProfessors(): List<Professor> {
        return try {
            val snapshot = firestore.collection("professors").get().await()

            if (snapshot.isEmpty) {
                Log.d("FirestoreTest", "No hay profesores en Firestore. Se deben agregar manualmente.")
            }

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Professor::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al obtener profesores: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun saveProfessor(professor: Professor) {
        try {
            val docRef = firestore.collection("professors").document()
            val professorWithId = professor.copy(id = docRef.id)
            docRef.set(professorWithId).await()
            Log.d("FirestoreTest", "Profesor guardado correctamente en Firestore")
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al guardar profesor: ${e.message}", e)
        }
    }
}
