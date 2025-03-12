package com.example.loginycardview.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.loginycardview.databinding.ActivityRegisterBinding
import com.example.loginycardview.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            val username = binding.editTextUsernameRegister.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPasswordRegister.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Por favor, completa todos los campos")
                return@setOnClickListener
            }

            authViewModel.registerUser(email, password, username) { success, message ->
                if (success) {
                    showToast("Registro exitoso. Verifica tu correo electrónico.")
                    navigateToLogin()
                } else {
                    showToast("Error al registrar: $message")
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
