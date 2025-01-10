package com.example.loginycardview.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginycardview.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val editTextUsername = findViewById<EditText>(R.id.editTextUsernameRegister)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPasswordRegister)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)

        buttonSave.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Al registrar al usuario, guardamos el nombre de usuario en SharedPreferences
                    val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("username", username) // Guardar el nombre de usuario
                        putString("email", email) // Guardar el email
                        putBoolean("isLoggedIn", true) // Indicar que el usuario está logueado
                        apply()
                    }

                    // Enviar correo de verificación
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Registro exitoso. Verifica tu correo", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        buttonCancel.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
