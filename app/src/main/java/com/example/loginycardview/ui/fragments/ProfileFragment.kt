package com.example.loginycardview.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var profileImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("username", "Invitado")
        val userEmail = sharedPref?.getString("email", "No registrado")
        val userProfileImage = sharedPref?.getString("profileImage", "")

        // Enlazar vistas
        userNameTextView = view.findViewById(R.id.user_name)
        userEmailTextView = view.findViewById(R.id.user_email)
        profileImageView = view.findViewById(R.id.profile_image)

        // Configurar texto de usuario
        userNameTextView.text = userName
        userEmailTextView.text = userEmail

        // Cargar la imagen de perfil con Glide
        Glide.with(this)
            .load(userProfileImage) // Ruta de la imagen de perfil desde SharedPreferences
            .placeholder(R.drawable.ic_placeholder) // Imagen mientras carga
            .error(R.drawable.ic_error) // Imagen si falla
            .into(profileImageView)

        // Agregar botón de edición de perfil
        view.findViewById<MaterialButton>(R.id.btn_edit_profile).setOnClickListener {
            openEditProfileDialog(userName, userEmail, userProfileImage)
        }
    }

    private fun openEditProfileDialog(userName: String?, userEmail: String?, userProfileImage: String?) {
        // Crear un diálogo para editar el perfil
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.edit_name)
        val emailEditText = dialogView.findViewById<EditText>(R.id.edit_email)
        val imageEditText = dialogView.findViewById<EditText>(R.id.edit_image)

        // Prellenar los campos con los datos actuales
        nameEditText.setText(userName)
        emailEditText.setText(userEmail)
        imageEditText.setText(userProfileImage)

        // Crear el diálogo
        AlertDialog.Builder(requireContext())
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                // Guardar los nuevos valores
                val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    this?.putString("username", nameEditText.text.toString())
                    this?.putString("email", emailEditText.text.toString())
                    this?.putString("profileImage", imageEditText.text.toString())
                    this?.apply()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
