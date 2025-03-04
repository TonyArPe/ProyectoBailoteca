package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginycardview.R
import com.example.loginycardview.presentation.viewmodel.ProfessorViewModel
import com.example.loginycardview.utils.ProfessorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrincipalFragment : Fragment(R.layout.fragment_principal) {

    private val professorViewModel: ProfessorViewModel by viewModels()
    private lateinit var professorAdapter: ProfessorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "La Bailoteca"
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        professorAdapter = ProfessorAdapter()
        recyclerView.adapter = professorAdapter

        observeProfessors()

        // 🔹 Forzamos la carga de datos desde Firestore
        professorViewModel.loadProfessors()
    }

    private fun observeProfessors() {
        viewLifecycleOwner.lifecycleScope.launch {
            professorViewModel.professors.collect { professors ->
                if (professors.isNotEmpty()) {
                    professorAdapter.updateProfessors(professors)
                } else {
                    Log.e("PrincipalFragment", "No se encontraron profesores en Firestore")
                }
            }
        }
    }

    private fun showAddProfessorDialog() {
        val addDialog = AddProfessorDialogFragment { newProfessor ->
            val professor = com.example.loginycardview.domain.Professor( // 🔹 Referencia correcta
                imageResId = newProfessor.imageResId ?: 0, // 🔹 Asegurar que no sea nulo
                name = newProfessor.username, // 🔹 Asegurar que `username` es el nombre
                specialty = newProfessor.specialty,
                isTopRated = newProfessor.isTopRated,
                description = newProfessor.description,
                email = newProfessor.email
            )
            professorViewModel.saveProfessor(professor)
            Toast.makeText(requireContext(), "Profesor agregado", Toast.LENGTH_SHORT).show()
        }
        addDialog.show(parentFragmentManager, "AddProfessorDialog")
    }

}
