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
import com.example.loginycardview.databinding.FragmentPrincipalBinding
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.presentation.viewmodel.ProfessorViewModel
import com.example.loginycardview.ui.dialogs.EditProfessorDialogFragment
import com.example.loginycardview.utils.ProfessorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrincipalFragment : Fragment(R.layout.fragment_principal) {

    private val professorViewModel: ProfessorViewModel by viewModels()
    private lateinit var professorAdapter: ProfessorAdapter
    private var _binding: FragmentPrincipalBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrincipalBinding.bind(view)

        requireActivity().title = "La Bailoteca"

        // Configurar RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        professorAdapter = ProfessorAdapter(::showEditProfessorDialog, ::deleteProfessor)
        binding.recyclerView.adapter = professorAdapter

        observeProfessors() // 🔹 Observamos los cambios en Firestore

        professorViewModel.loadProfessors() // 🔹 Cargamos los profesores al iniciar

        // Botón para agregar un nuevo profesor
        binding.fabAdd.setOnClickListener { showAddProfessorDialog() }
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
            val professor = Professor(
                id = "", // Firestore asignará un ID automáticamente
                imageUrl = newProfessor.imageUrl,
                name = newProfessor.name,
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

    private fun showEditProfessorDialog(professor: Professor) {
        val editDialog = EditProfessorDialogFragment(professor) { updatedProfessor ->
            professorViewModel.saveProfessor(updatedProfessor)
            Toast.makeText(requireContext(), "Profesor actualizado", Toast.LENGTH_SHORT).show()
        }
        editDialog.show(parentFragmentManager, "EditProfessorDialog")
    }

    private fun deleteProfessor(professor: Professor) {
        professorViewModel.deleteProfessor(professor.id)
        Toast.makeText(requireContext(), "Profesor eliminado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
