package com.example.loginycardview.ui.fragments

import ProfessorViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.utils.ProfessorAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfessorFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var professorAdapter: ProfessorAdapter
    private val viewModel: ProfessorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_professor, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_professors)
        setupRecyclerView()
        observeProfessors()
        return view
    }

    private fun setupRecyclerView() {
        professorAdapter = ProfessorAdapter(mutableListOf()) // Usar una lista mutable
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = professorAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadProfessors() // Asegúrate de que el ViewModel tiene esta función
    }


    private fun observeProfessors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.professors.collectLatest { professors ->
                professorAdapter.updateData(professors) // No hagas conversiones aquí
            }
        }
    }
}
