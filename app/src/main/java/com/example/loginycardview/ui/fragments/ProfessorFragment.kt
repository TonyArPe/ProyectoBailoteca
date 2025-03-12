package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginycardview.databinding.FragmentProfessorBinding
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.presentation.viewmodel.ProfessorViewModel
import com.example.loginycardview.ui.dialogs.EditProfessorDialogFragment
import com.example.loginycardview.utils.ProfessorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfessorFragment : Fragment() {

    private var _binding: FragmentProfessorBinding? = null
    private val binding get() = _binding!!
    private val professorViewModel: ProfessorViewModel by viewModels()
    private lateinit var professorAdapter: ProfessorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfessorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        professorViewModel.loadProfessors()

        binding.btnAddProfessor.setOnClickListener {
            professorViewModel.saveProfessor(createDummyProfessor())
        }
    }

    private fun setupRecyclerView() {
        professorAdapter = ProfessorAdapter(
            onEdit = { professor -> showEditProfessorDialog(professor) }, // ✅ Ahora existe la función
            onDelete = { professor -> professorViewModel.deleteProfessor(professor.id) } // ✅ Pasamos solo el ID
        )
        binding.recyclerViewProfessors.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProfessors.adapter = professorAdapter
    }

    /**
     * Muestra un diálogo para editar un profesor
     */
    private fun showEditProfessorDialog(professor: Professor) {
        val editDialog = EditProfessorDialogFragment(professor) { updatedProfessor ->
            professorViewModel.saveProfessor(updatedProfessor) // ✅ Guardamos el profesor actualizado
        }
        editDialog.show(parentFragmentManager, "EditProfessorDialog")
    }



    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            professorViewModel.professors.collect { professors ->
                professorAdapter.updateProfessors(professors)
            }
        }
    }

    private fun createDummyProfessor() = Professor(
        id = "", // 🔹 Firestore asignará un ID automáticamente
        imageUrl = "", // 🔹 Se puede dejar vacío o asignar una URL predeterminada
        name = "Nuevo Profesor",
        specialty = "Danza",
        isTopRated = true,
        description = "Profesor de prueba generado automáticamente.",
        email = "test@example.com"
    )


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
