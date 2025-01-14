package com.example.loginycardview.ui.activitys

import com.example.loginycardview.data.Professor
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginycardview.R

class ProfessorProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_profile)

        // Recuperamos el objeto 'com.example.loginycardview.data.Professor' pasado en el Intent
        val professor = intent.getParcelableExtra<Professor>("professor")

        // Referencias a los elementos de la vista
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val specialtyTextView = findViewById<TextView>(R.id.specialtyTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val imageView = findViewById<ImageView>(R.id.professorImageView)
        val contactButton: Button = findViewById(R.id.contactButton)
        val backButton:Button = findViewById(R.id.backButton)

        // Asignamos los datos del profesor a los elementos de la vista
        nameTextView.text = professor?.name
        specialtyTextView.text = "Especialidad: ${professor?.specialty}"
        descriptionTextView.text = "Descripción: ${professor?.description}"
        emailTextView.text = "Email: ${professor?.email}"

        // Cargar la imagen del profesor usando Glide
        Glide.with(this)
            .load(professor?.imageResId)
            .into(imageView)

        // Configurar el botón de contacto
        contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(professor?.email))  // Usamos el email del profesor
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases")
            intent.putExtra(Intent.EXTRA_TEXT, "Hola ${professor?.name}, estoy interesado en tus clases de ${professor?.specialty}.")

            // Verificamos si hay una aplicación para enviar correos
            startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        // Configurar el botón de retroceso
        backButton.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }
    }
}
