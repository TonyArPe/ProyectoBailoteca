package com.example.loginycardview.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loginycardview.databinding.ActivityLoginBinding
import com.example.loginycardview.presentation.viewmodel.AuthViewModel
import com.example.loginycardview.ui.activitys.MainActivity
import com.example.loginycardview.ui.activitys.RegisterActivity
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
            val email = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Completa todos los campos")
                return@setOnClickListener
            }

            authViewModel.login(email, password) { success ->
                if (success) {
                    navigateToMain()
                } else {
                    showToast("Error en login")
                }
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
                navigateToMain()
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
}
