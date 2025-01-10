package com.example.loginycardview.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginycardview.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonForgotPassword = findViewById<Button>(R.id.buttonForgotPassword)
        val editTextUsername = findViewById<AutoCompleteTextView>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        // Verificar si el usuario ya está logueado
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }

        buttonLogin.setOnClickListener {
            val email = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Guardar el estado del usuario logueado en SharedPreferences
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }
                        startActivity(Intent(this, PrincipalActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Por favor, verifica tu correo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonForgotPassword.setOnClickListener {
            val email = editTextUsername.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Introduce tu correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo de restauración enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al enviar correo: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
