package com.example.loginycardview

import Professor
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PrincipalActivity : AppCompatActivity() {

    private lateinit var professorList: MutableList<Professor>
    private lateinit var adapter: CardViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        // Obtener el nombre de usuario desde SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", null)

        // Verificar que el valor no sea null
        if (username != null) {
            val textViewUser = findViewById<TextView>(R.id.textViewUser)
            textViewUser.text = "Bienvenido, $username!"
        } else {
            Toast.makeText(this, "Error: No se encontraron datos del usuario", Toast.LENGTH_SHORT)
                .show()
            finish()
        }

        // Configuración del RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

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


        // Botones flotantes
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener { showAddDialog() }

    }

    // Función para mostrar el diálogo de añadir profesor
    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_professor, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Añadir Profesor")
            .setView(dialogView)
            .setPositiveButton("Añadir") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.editName).text.toString()
                val specialty = dialogView.findViewById<EditText>(R.id.editSpecialty).text.toString()
                val description = dialogView.findViewById<EditText>(R.id.editDescription).text.toString()
                val email = dialogView.findViewById<EditText>(R.id.editEmail).text.toString()
                val isTopRated = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated).isChecked

                professorList.add(
                    Professor(
                        R.drawable.professor_placeholder,
                        name,
                        specialty,
                        isTopRated,
                        description,
                        email
                    )
                )
                adapter.notifyItemInserted(professorList.size - 1)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }
}
