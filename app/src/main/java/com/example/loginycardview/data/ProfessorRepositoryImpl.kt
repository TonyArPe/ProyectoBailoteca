package com.example.loginycardview.data

import android.util.Log
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfessorRepositoryImpl : ProfessorRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getProfessors(): List<Professor> {
        return try {
            val snapshot = firestore.collection("professors").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Professor::class.java)?.copy(id = doc.id) // 🔹 Asigna el ID desde Firestore
            }
        } catch (e: Exception) {
            Log.e("ProfessorRepositoryImpl", "Error al obtener profesores", e)
            emptyList()
        }
    }

    override suspend fun saveProfessor(professor: Professor) {
        try {
            val docRef = firestore.collection("professors").document()
            val professorWithId = professor.copy(id = docRef.id)
            docRef.set(professorWithId).await()
            Log.d("ProfessorRepositoryImpl", "Profesor guardado correctamente en Firestore")
        } catch (e: Exception) {
            Log.e("ProfessorRepositoryImpl", "Error al guardar profesor en Firestore", e)
        }
    }
}
