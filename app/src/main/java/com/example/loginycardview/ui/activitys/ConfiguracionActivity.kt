package com.example.loginycardview.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.google.android.material.button.MaterialButton

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        // Inicializar vistas
        userNameTextView = findViewById(R.id.user_name)
        userEmailTextView = findViewById(R.id.user_email)
        profileImageView = findViewById(R.id.profile_image)
        saveButton = findViewById(R.id.btn_save_changes)

        // Recuperar SharedPreferences para el perfil
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("username", "Invitado")
        val userEmail = sharedPref.getString("email", "No registrado")
        val userProfileImage = sharedPref.getString("profileImage", "")

        // Configurar los datos en la UI
        userNameTextView.text = userName
        userEmailTextView.text = userEmail

        Glide.with(this)
            .load(userProfileImage)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(profileImageView)

        // Acción para editar el perfil
        findViewById<MaterialButton>(R.id.btn_edit_profile)?.setOnClickListener {
            openEditProfileDialog(userName, userEmail, userProfileImage)
        }

        // Acción para guardar cambios
        saveButton.setOnClickListener {
            saveUserProfileChanges()
        }
    }

    private fun openEditProfileDialog(userName: String?, userEmail: String?, userProfileImage: String?) {
        // Crear un diálogo para editar el perfil
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.edit_name)
        val emailEditText = dialogView.findViewById<EditText>(R.id.edit_email)
        val imageEditText = dialogView.findViewById<EditText>(R.id.edit_image)

        // Prellenar los campos con los datos actuales
        nameEditText.setText(userName)
        emailEditText.setText(userEmail)
        imageEditText.setText(userProfileImage)

        // Crear el diálogo
        AlertDialog.Builder(this)
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                // Guardar los nuevos valores en SharedPreferences
                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("username", nameEditText.text.toString())
                    putString("email", emailEditText.text.toString())
                    putString("profileImage", imageEditText.text.toString())
                    apply()
                }
                // Actualizar la UI después de guardar los cambios
                updateUIAfterProfileChanges(nameEditText.text.toString(), emailEditText.text.toString(), imageEditText.text.toString())
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateUIAfterProfileChanges(userName: String, userEmail: String, userProfileImage: String) {
        // Actualizar la UI de la actividad
        userNameTextView.text = userName
        userEmailTextView.text = userEmail
        Glide.with(this)
            .load(userProfileImage)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(profileImageView)
    }

    private fun saveUserProfileChanges() {
        // Guardar cambios (aquí puedes agregar más lógica si es necesario)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", userNameTextView.text.toString())
            putString("email", userEmailTextView.text.toString())
            putString("profileImage", "")
            apply()
        }
    }
}
