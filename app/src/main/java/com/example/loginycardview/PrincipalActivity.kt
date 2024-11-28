package com.example.loginycardview

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
            Professor(R.drawable.professor1, "Juan Pérez", "Salsa", true),
            Professor(R.drawable.professor2, "Ana Gómez", "Bachata", false),
            Professor(R.drawable.professor3, "Carlos López", "Flamenco", true),
            Professor(R.drawable.professor4, "María García", "Tango", false),
            Professor(R.drawable.professor5, "Luis Martínez", "Ballet", true)
        )

        // Configurar el adapter
        adapter = CardViewAdapter(professorList)
        recyclerView.adapter = adapter

        // Botones flotantes
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener { showAddDialog() }
        findViewById<FloatingActionButton>(R.id.fab_update).setOnClickListener { showUpdateDialog() }
        findViewById<FloatingActionButton>(R.id.fab_delete).setOnClickListener { showDeleteDialog() }
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
                val isTopRated = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated).isChecked
                professorList.add(
                    Professor(
                        R.drawable.professor_placeholder,
                        name,
                        specialty,
                        isTopRated
                    )
                )
                adapter.notifyItemInserted(professorList.size - 1)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    // Función para mostrar el diálogo de actualizar profesor
    private fun showUpdateDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_professor, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Actualizar Profesor")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val position = dialogView.findViewById<EditText>(R.id.editPosition).text.toString().toIntOrNull()
                if (position != null && position in professorList.indices) {
                    val name = dialogView.findViewById<EditText>(R.id.editName).text.toString()
                    val specialty = dialogView.findViewById<EditText>(R.id.editSpecialty).text.toString()
                    val isTopRated = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated).isChecked
                    professorList[position] = Professor(R.drawable.professor_placeholder, name, specialty, isTopRated)
                    adapter.notifyItemChanged(position)
                } else {
                    Toast.makeText(this, "Posición inválida", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    // Función para mostrar el diálogo de eliminar profesor
    private fun showDeleteDialog() {
        val editText = EditText(this).apply { hint = "Posición" }
        val dialog = AlertDialog.Builder(this)
            .setTitle("Eliminar Profesor")
            .setMessage("Ingrese la posición del profesor a eliminar:")
            .setView(editText)
            .setPositiveButton("Eliminar") { _, _ ->
                val position = editText.text.toString().toIntOrNull()
                if (position != null && position in professorList.indices) {
                    professorList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                } else {
                    Toast.makeText(this, "Posición inválida", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }
}
