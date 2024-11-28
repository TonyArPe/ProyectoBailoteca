package com.example.loginycardview

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PrincipalActivity : AppCompatActivity() {

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
            Toast.makeText(this, "Error: No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configuración del RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lista de profesores
        val professorList = listOf(
            Professor(R.drawable.professor1, "Juan Pérez", "Salsa", true),
            Professor(R.drawable.professor2, "Ana Gómez", "Bachata", false),
            Professor(R.drawable.professor3, "Carlos López", "Flamenco", true),
            Professor(R.drawable.professor4, "María García", "Tango", false),
            Professor(R.drawable.professor5, "Luis Martínez", "Ballet", true),
            Professor(R.drawable.professor6, "Marta Rodríguez", "Kizomba", false),
            Professor(R.drawable.professor7, "Carla Sánchez", "Hip Hop", false),
            Professor(R.drawable.professor8, "Juanita Pérez", "Merengue", true),
            Professor(R.drawable.professor9, "José Martínez", "Rumba", false),
            Professor(R.drawable.professor10, "Lucía González", "Jazz", true),
            Professor(R.drawable.professor11, "Carlos Ruiz", "Dancehall", false),
            Professor(R.drawable.professor12, "Pedro Fernández", "Vals", true),
            Professor(R.drawable.professor13, "Lucía Álvarez", "Cha Cha", false)
        )


        // Configurar el adapter
        val adapter = CardViewAdapter(professorList)
        recyclerView.adapter = adapter


    }


}
