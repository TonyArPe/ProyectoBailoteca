package com.example.loginycardview.domain

interface ProfessorRepository {
    suspend fun getProfessors(): List<Professor>
    suspend fun saveProfessor(professor: Professor)
}