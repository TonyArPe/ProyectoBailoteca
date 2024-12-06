package com.example.loginycardview

import Professor
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ProfessorProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_profile)

        val professor = intent.getParcelableExtra<Professor>("professor")

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val specialtyTextView = findViewById<TextView>(R.id.specialtyTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val imageView = findViewById<ImageView>(R.id.professorImageView)

        nameTextView.text = professor?.name
        specialtyTextView.text = "Especialidad: ${professor?.specialty}"
        descriptionTextView.text = "Descripción: ${professor?.description}"
        emailTextView.text = "Email: ${professor?.email}"

        Glide.with(this)
            .load(professor?.imageResId)
            .into(imageView)
    }
}
