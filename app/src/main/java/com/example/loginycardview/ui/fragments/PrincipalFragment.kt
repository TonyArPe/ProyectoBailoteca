package com.example.loginycardview.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
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

        // Cambiar el título del Toolbar
        requireActivity().title = "La Bailoteca"

        // Obtener los datos del usuario desde SharedPreferences
        val sharedPref = activity?.getSharedPreferences("userSettings", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "Usuario")  // Valor por defecto si no se ha configurado
        val email = sharedPref?.getString("email", "email@dominio.com")  // Valor por defecto si no se ha configurado
        val profileImageUri = sharedPref?.getString("uri", null)  // URL de la imagen de perfil, por defecto null

        // Actualizar el texto de bienvenida
        val textViewUser = view.findViewById<TextView>(R.id.textViewUser)
        textViewUser?.text = "Bienvenido, $username!"

        // Actualizar los datos del header
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtEmail = view.findViewById<TextView>(R.id.txt_email)
        txtName?.text = username
        txtEmail?.text = email

        // Actualizar la imagen del perfil en el header
        val imageView = view.findViewById<ImageView>(R.id.image_perfil)
        if (profileImageUri != null) {
            // Si la URI de la imagen está presente, cargarla con Glide
            Glide.with(requireContext())
                .load(Uri.parse(profileImageUri))
                .placeholder(R.mipmap.ic_launcher_foreground)  // Imagen por defecto mientras se carga
                //.into(imageView)
        } else {
            // Si no hay URI, mostrar la imagen por defecto
            imageView?.setImageResource(R.mipmap.ic_launcher_foreground)
        }

        // Configuración del RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // Crear lista mutable de profesores
        professorList = mutableListOf(
            Professor(
                R.drawable.professor1,
                "Juan Pérez",
                "Salsa",
                true,
                "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.",
                "juan.perez@example.com"
            ),
            Professor(
                R.drawable.professor2,
                "Ana Gómez",
                "Bachata",
                false,
                "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.",
                "ana.gomez@example.com"
            ),
            Professor(
                R.drawable.professor3,
                "Carlos López",
                "Flamenco",
                true,
                "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.",
                "carlos.lopez@example.com"
            ),
            Professor(
                R.drawable.professor4,
                "María García",
                "Tango",
                false,
                "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.",
                "maria.garcia@example.com"
            ),
            Professor(
                R.drawable.professor5,
                "Luis Martínez",
                "Ballet",
                true,
                "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.",
                "luis.martinez@example.com"
            ),
            Professor(
                R.drawable.professor6,
                "Isabel Ruiz",
                "Contemporáneo",
                true,
                "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.",
                "isabel.ruiz@example.com"
            ),
            Professor(
                R.drawable.professor7,
                "Miguel Sánchez",
                "Hip Hop",
                false,
                "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.",
                "miguel.sanchez@example.com"
            ),
            Professor(
                R.drawable.professor8,
                "Daniel López",
                "Kizomba",
                true,
                "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.",
                "daniel.lopez@example.com"
            )
        )

        // Configurar el adapter
        adapter = CardViewAdapter(professorList)
        recyclerView.adapter = adapter

        // Botón flotante para mostrar el menú
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener { showBottomNavigationDrawer() }
    }

    private fun showBottomNavigationDrawer() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_navigation, null)

        // Sección Añadir Profesor
        bottomSheetView.findViewById<View>(R.id.add_professor_option).setOnClickListener {
            bottomSheetDialog.dismiss()
            showAddProfessorDialog()
        }

        // Sección Horarios
        bottomSheetView.findViewById<View>(R.id.schedule_class_option).setOnClickListener {
            bottomSheetDialog.dismiss()
            showCalendarDialog()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showAddProfessorDialog() {
        val addDialog = AddProfessorDialogFragment { newProfessor ->
            professorList.add(newProfessor)
            adapter.notifyItemInserted(professorList.size - 1)
        }
        addDialog.show(parentFragmentManager, "AddProfessorDialog")
    }

    private fun showCalendarDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Después de seleccionar la fecha, mostrar el TimePicker
                showTimePickerDialog(selectedYear, selectedMonth, selectedDayOfMonth)
            },
            year, month, dayOfMonth
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crear un TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Después de seleccionar la hora, mostrar el mensaje de confirmación
                val selectedDateTime = "$dayOfMonth/${month + 1}/$year a las $selectedHour:$selectedMinute"
                Toast.makeText(requireContext(), "Clase reservada para: $selectedDateTime", Toast.LENGTH_LONG).show()

                // Aquí puedes agregar la lógica para guardar la reserva (a través de un backend, SharedPreferences, etc.)
            },
            hour, minute, true
        )

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }
}
