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
import com.example.loginycardview.ui.dialogs.EventDialogFragment
import com.example.loginycardview.utils.EventAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter(emptyList(), ::showEditDialog, ::deleteEvent)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventAdapter

        eventViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.updateEvents(events)
        }

        binding.buttonAddEvent.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        EventDialogFragment { event -> eventViewModel.addEvent(event) }
            .show(parentFragmentManager, "AddEventDialog")
    }

    private fun showEditDialog(event: Event) {
        EventDialogFragment(event) { updatedEvent -> eventViewModel.updateEvent(updatedEvent) }
            .show(parentFragmentManager, "EditEventDialog")
    }

    private fun deleteEvent(eventId: String) {
        eventViewModel.deleteEvent(eventId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks
    }
}
