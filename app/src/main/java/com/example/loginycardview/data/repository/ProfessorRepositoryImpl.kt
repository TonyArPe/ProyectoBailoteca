package com.example.loginycardview.data.repository

import android.util.Log
import com.example.loginycardview.R
import com.example.loginycardview.data.local.dao.ProfessorDao
import com.example.loginycardview.data.local.entities.ProfessorEntity
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class ProfessorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProfessorRepository {

    private val professorList = listOf(
        Professor(R.drawable.professor1, "Juan Pérez", "Salsa", true, "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.", "juan.perez@example.com"),
        Professor(R.drawable.professor2, "Ana Gómez", "Bachata", false, "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.", "ana.gomez@example.com"),
        Professor(R.drawable.professor3, "Carlos López", "Flamenco", true, "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.", "carlos.lopez@example.com"),
        Professor(R.drawable.professor4, "María García", "Tango", false, "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.", "maria.garcia@example.com"),
        Professor(R.drawable.professor5, "Luis Martínez", "Ballet", true, "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.", "luis.martinez@example.com"),
        Professor(R.drawable.professor6, "Isabel Ruiz", "Contemporáneo", true, "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.", "isabel.ruiz@example.com"),
        Professor(R.drawable.professor7, "Miguel Sánchez", "Hip Hop", false, "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.", "miguel.sanchez@example.com"),
        Professor(R.drawable.professor8, "Daniel López", "Kizomba", true, "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.", "daniel.lopez@example.com")
    )

    override suspend fun getProfessors(): List<Professor> {
        return try {
            val snapshot = firestore.collection("professors").get().await()

            // Si la base de datos está vacía, subimos los profesores por defecto
            if (snapshot.isEmpty) {
                professorList.forEach { saveProfessor(it) }
            }

            // Volvemos a obtener los datos después de subirlos
            firestore.collection("professors").get().await().documents.mapNotNull {
                it.toObject(Professor::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al obtener profesores: ${e.message}", e)
            emptyList()
        }
    }


    override suspend fun saveProfessor(professor: Professor) {
        try {
            firestore.collection("professors").add(professor).await()
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al guardar profesor: ${e.message}", e)
        }
    }
}
