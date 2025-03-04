package com.example.loginycardview.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.usecases.GetProfessorsUseCase
import com.example.loginycardview.domain.usecases.SaveProfessorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfessorViewModel @Inject constructor(
    private val getProfessorsUseCase: GetProfessorsUseCase,
    private val saveProfessorUseCase: SaveProfessorUseCase
) : ViewModel() {

    private val _professors = MutableStateFlow<List<Professor>>(emptyList())
    val professors: StateFlow<List<Professor>> get() = _professors

    fun loadProfessors() {
        viewModelScope.launch {
            val professorsList = getProfessorsUseCase()
            _professors.value = professorsList // 🔹 Actualizamos el estado
            Log.d("ProfessorViewModel", "Profesores obtenidos: ${professorsList.size}")
        }
    }


    fun saveProfessor(professor: Professor) {
        viewModelScope.launch {
            saveProfessorUseCase(professor)
            loadProfessors()
        }
    }
}
