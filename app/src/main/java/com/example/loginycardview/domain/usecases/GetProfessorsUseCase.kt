package com.example.loginycardview.domain.usecases

import com.example.loginycardview.domain.Professor
import com.example.loginycardview.domain.ProfessorRepository
import javax.inject.Inject

class GetProfessorsUseCase @Inject constructor(
    private val professorRepository: ProfessorRepository
) {
    suspend operator fun invoke(): List<Professor> = professorRepository.getProfessors()
}
