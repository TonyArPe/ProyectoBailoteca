package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Event
import com.example.loginycardview.utils.EventAdapter

class EventFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val eventList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_events)
        setupRecyclerView()
        loadEvents()
        return view
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(eventList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = eventAdapter
    }

    private fun loadEvents() {
        eventList.add(Event("Clase de Salsa", "2025-01-20", "Clase especial de salsa con profesores invitados."))
        eventList.add(Event("Competencia Interna", "2025-02-10", "Evento de competencia entre estudiantes de la academia."))
        eventAdapter.notifyDataSetChanged()
    }
}
