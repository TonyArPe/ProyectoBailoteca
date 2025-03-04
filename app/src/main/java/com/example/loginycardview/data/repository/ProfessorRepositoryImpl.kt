package com.example.loginycardview.data.repository

import com.example.loginycardview.R
import com.example.loginycardview.data.local.dao.ProfessorDao
import com.example.loginycardview.data.local.entities.ProfessorEntity
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ProfessorRepositoryImpl @Inject constructor(
    private val professorDao: ProfessorDao
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
        return withContext(Dispatchers.IO) {
            val dbProfessors = professorDao.getAllProfessors()
            if (dbProfessors.isEmpty()) {
                professorList.forEach { saveProfessor(it) }
            }
            professorDao.getAllProfessors().map {
                Professor(it.imageResId, it.name, it.specialty, it.isTopRated, it.description, it.email)
            }
        }
    }

    override suspend fun saveProfessor(professor: Professor) {
        withContext(Dispatchers.IO) {
            professorDao.insertProfessor(
                ProfessorEntity(
                    imageResId = professor.imageResId,
                    name = professor.name,
                    specialty = professor.specialty,
                    isTopRated = professor.isTopRated,
                    description = professor.description,
                    email = professor.email
                )
            )
        }
    }
}
