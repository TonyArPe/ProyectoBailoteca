package com.example.loginycardview.data

import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository

class ProfessorRepositoryImpl : ProfessorRepository {
    override suspend fun getProfessors(): List<Professor> {
        return listOf(
            Professor(
                imageResId = R.drawable.professor1,
                name = "Juan Pérez",
                specialty = "Salsa",
                isTopRated = true,
                description = "Instructor experto en Salsa Cubana",
                email = "juan@example.com"
            ),
            Professor(
                imageResId = R.drawable.professor2,
                name = "María López",
                specialty = "Bachata",
                isTopRated = false,
                description = "Especialista en bachata sensual",
                email = "maria@example.com"
            )
        )
    }


    override suspend fun saveProfessor(professor: Professor) {
        // Aquí iría la lógica para guardar un profesor en la base de datos
    }
}