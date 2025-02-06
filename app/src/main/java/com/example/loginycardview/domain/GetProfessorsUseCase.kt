package com.example.loginycardview.domain

class GetProfessorsUseCase(private val professorRepository: ProfessorRepository) {
    suspend operator fun invoke(): List<Professor> {
        return professorRepository.getProfessors()
    }
}