package com.example.loginycardview.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loginycardview.databinding.ActivityLoginBinding
import com.example.loginycardview.presentation.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Redirigir a MainActivity después del login exitoso
                            startActivity(Intent(this, MainActivity::class.java))
                            finish() // 🔹 Evita que el usuario regrese a LoginActivity
                        } else {
                            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonForgotPassword.setOnClickListener {
            val email = binding.editTextUsername.text.toString().trim()
            if (email.isEmpty()) {
                showToast("Introduce tu correo")
                return@setOnClickListener
            }

            authViewModel.resetPassword(email) {
                showToast(if (it) "Correo enviado" else "Error al enviar")
            }
        }

        binding.buttonGuestLogin.setOnClickListener {
            authViewModel.loginAsGuest {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("isGuestUser", true) // 🔹 Pasamos la info de que es invitado
                startActivity(intent)
                finish()
            }
        }

    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}


