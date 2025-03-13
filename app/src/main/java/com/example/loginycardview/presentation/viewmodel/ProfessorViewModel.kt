package com.example.loginycardview.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.usecases.GetProfessorsUseCase
import com.example.loginycardview.domain.usecases.SaveProfessorUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfessorViewModel @Inject constructor(
    private val getProfessorsUseCase: GetProfessorsUseCase,
    private val saveProfessorUseCase: SaveProfessorUseCase
) : ViewModel() {

    private val _professors = MutableStateFlow<List<Professor>>(emptyList())
    val professors: StateFlow<List<Professor>> get() = _professors

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference


    /**
     * Cargar profesores desde Firestore y actualizar estado.
     */
    fun loadProfessors() {
        viewModelScope.launch {
            try {
                firestore.collection("professors").addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("ProfessorViewModel", "Error al obtener profesores", e)
                        return@addSnapshotListener
                    }

                    val professorList = snapshot?.documents?.mapNotNull { doc ->
                        val professor = doc.toObject(Professor::class.java)
                        professor?.copy(id = doc.id) // 🔹 Asegurar que se asigne el ID del documento
                    } ?: emptyList()

                    _professors.value = professorList
                    Log.d("ProfessorViewModel", "Profesores obtenidos: ${professorList.size}")
                }
            } catch (e: Exception) {
                Log.e("ProfessorViewModel", "Error al obtener profesores", e)
            }
        }
    }


    /**
     * Inserta una lista predeterminada de profesores si la base de datos está vacía.
     */
    fun insertDefaultProfessors() {
        firestore.collection("professors").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                    val defaultProfessors = listOf(
                        mapOf(
                            "name" to "Juan Pérez",
                            "specialty" to "Salsa",
                            "isTopRated" to true,
                            "description" to "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.",
                            "email" to "juan.perez@example.com"
                        ),
                        mapOf(
                            "name" to "Ana Gómez",
                            "specialty" to "Bachata",
                            "isTopRated" to false,
                            "description" to "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.",
                            "email" to "ana.gomez@example.com"
                        ),
                        mapOf(
                            "name" to "Carlos López",
                            "specialty" to "Flamenco",
                            "isTopRated" to true,
                            "description" to "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.",
                            "email" to "carlos.lopez@example.com"
                        ),
                        mapOf(
                            "name" to "María García",
                            "specialty" to "Tango",
                            "isTopRated" to false,
                            "description" to "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.",
                            "email" to "maria.garcia@example.com"
                        ),
                        mapOf(
                            "name" to "Luis Martínez",
                            "specialty" to "Ballet",
                            "isTopRated" to true,
                            "description" to "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.",
                            "email" to "luis.martinez@example.com"
                        ),
                        mapOf(
                            "name" to "Isabel Ruiz",
                            "specialty" to "Contemporáneo",
                            "isTopRated" to true,
                            "description" to "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.",
                            "email" to "isabel.ruiz@example.com"
                        ),
                        mapOf(
                            "name" to "Miguel Sánchez",
                            "specialty" to "Hip Hop",
                            "isTopRated" to false,
                            "description" to "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.",
                            "email" to "miguel.sanchez@example.com"
                        ),
                        mapOf(
                            "name" to "Daniel López",
                            "specialty" to "Kizomba",
                            "isTopRated" to true,
                            "description" to "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.",
                            "email" to "daniel.lopez@example.com"
                        )
                    )

                defaultProfessors.forEach { professor ->
                    firestore.collection("professors").add(professor)
                }
                Log.d("ProfessorViewModel", "Profesores predeterminados insertados correctamente.")
            }
        }
    }


    /**
     * Guardar un profesor en Firestore.
     */
    fun saveProfessor(professor: Professor) {
        val db = FirebaseFirestore.getInstance()
        db.collection("professors")
            .add(professor)
            .addOnSuccessListener {
                Log.d("Firestore", "Profesor guardado con éxito")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar profesor", e)
            }
    }


    fun uploadImageToStorage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "professors/${System.currentTimeMillis()}.jpg" // 🔹 Nombre único basado en timestamp
        val imageRef = storageRef.child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("FirebaseStorage", "Imagen subida con éxito: $uri")
                    onSuccess(uri.toString()) // ✅ Devuelve la URL de la imagen
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseStorage", "Error al subir la imagen", exception)
                onFailure(exception)
            }
    }





    fun deleteProfessor(professorId: String) {
        viewModelScope.launch {
            try {
                FirebaseFirestore.getInstance().collection("professors").document(professorId).delete()
                    .await()
                loadProfessors() // 🔹 Recargar la lista después de eliminar
            } catch (e: Exception) {
                Log.e("ProfessorViewModel", "Error al eliminar profesor", e)
            }
        }
    }

}
