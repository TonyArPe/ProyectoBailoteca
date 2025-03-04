package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginycardview.databinding.FragmentEventBinding
import com.example.loginycardview.domain.Event
import com.example.loginycardview.presentation.viewmodel.EventViewModel
import com.example.loginycardview.utils.EventAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter // 🔹 Se inicializa más tarde

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        eventViewModel.loadEvents()

        binding.btnAddEvent.setOnClickListener {
            eventViewModel.saveEvent(createDummyEvent())
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter() // 🔹 Se inicializa correctamente sin parámetros
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEvents.adapter = eventAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            eventViewModel.events.collect { events ->
                eventAdapter.updateEvents(events) // 🔹 Se usa updateEvents() en lugar de submitList()
            }
        }
    }

    private fun createDummyEvent() = Event(
        title = "Nuevo Evento",
        date = "2025-03-15",
        description = "Este es un evento de prueba generado automáticamente."
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
