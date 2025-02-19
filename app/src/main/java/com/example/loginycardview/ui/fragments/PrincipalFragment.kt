package com.example.loginycardview.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.example.loginycardview.data.Professor
import com.example.loginycardview.utils.CardViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class PrincipalFragment : Fragment(R.layout.fragment_principal) {

    private lateinit var professorList: MutableList<Professor>
    private lateinit var adapter: CardViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "La Bailoteca"
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        professorList = mutableListOf(
            Professor(R.drawable.professor1, "Juan Pérez", "Salsa", true, "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.", "juan.perez@example.com"),
            Professor(R.drawable.professor2, "Ana Gómez", "Bachata", false, "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.", "ana.gomez@example.com"),
            Professor(R.drawable.professor3, "Carlos López", "Flamenco", true, "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.", "carlos.lopez@example.com"),
            Professor(R.drawable.professor4, "María García", "Tango", false, "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.", "maria.garcia@example.com"),
            Professor(R.drawable.professor5, "Luis Martínez", "Ballet", true, "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.", "luis.martinez@example.com"),
            Professor(R.drawable.professor6, "Isabel Ruiz", "Contemporáneo", true, "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.", "isabel.ruiz@example.com"),
            Professor(R.drawable.professor7, "Miguel Sánchez", "Hip Hop", false, "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.", "miguel.sanchez@example.com"),
            Professor(R.drawable.professor8, "Daniel López", "Kizomba", true, "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.", "daniel.lopez@example.com")
        )

        adapter = CardViewAdapter(professorList)
        recyclerView.adapter = adapter
        Log.d("PrincipalFragment", "Lista de profesores cargada en RecyclerView")
    }

    private fun showBottomNavigationDrawer() {
        Log.d("PrincipalFragment", "Abriendo BottomSheet")
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_navigation, null)

        bottomSheetView.findViewById<View>(R.id.add_professor_option).setOnClickListener {
            bottomSheetDialog.dismiss()
            showAddProfessorDialog()
        }

        bottomSheetView.findViewById<View>(R.id.schedule_class_option).setOnClickListener {
            bottomSheetDialog.dismiss()
            showCalendarDialog()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showAddProfessorDialog() {
        Log.d("PrincipalFragment", "Abriendo diálogo para agregar profesor")
        val addDialog = AddProfessorDialogFragment { newProfessor ->
            professorList.add(newProfessor)
            adapter.notifyItemInserted(professorList.size - 1)
            Log.d("PrincipalFragment", "Nuevo profesor agregado: ${newProfessor.username}")
        }
        addDialog.show(parentFragmentManager, "AddProfessorDialog")
    }

    private fun showCalendarDialog() {
        Log.d("PrincipalFragment", "Abriendo diálogo de calendario")
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                showTimePickerDialog(selectedYear, selectedMonth, selectedDayOfMonth)
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(year: Int, month: Int, dayOfMonth: Int) {
        Log.d("PrincipalFragment", "Abriendo diálogo de selección de hora")
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedDateTime = "$dayOfMonth/${month + 1}/$year a las $selectedHour:$selectedMinute"
                Toast.makeText(requireContext(), "Clase reservada para: $selectedDateTime", Toast.LENGTH_LONG).show()
                Log.d("PrincipalFragment", "Clase reservada para: $selectedDateTime")
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }
}