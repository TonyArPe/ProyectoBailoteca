package com.example.loginycardview.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatButton
import com.example.loginycardview.R
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import com.google.android.material.navigation.NavigationView

class ModificarPerfilFragment : Fragment(R.layout.fragment_modificar_perfil) {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var btnSave: AppCompatButton
    private val IMAGE_PICK_REQUEST_CODE = 1001  // Código para la solicitud de la imagen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener las vistas
        editTextName = view.findViewById(R.id.editTextName)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        btnSave = view.findViewById(R.id.btnSave)

        // Cargar los datos del usuario si están disponibles en SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        val email = sharedPref?.getString("email", "")
        val profilePicUri = sharedPref?.getString("profilePicUri", null)

        editTextName.setText(username)
        editTextEmail.setText(email)

        // Cargar la foto de perfil desde SharedPreferences
        profilePicUri?.let {
            val uri = Uri.parse(it)
            imageViewProfilePic.setImageURI(uri)
        }

        // Acción del botón "Guardar"
        btnSave.setOnClickListener {
            val newName = editTextName.text.toString()
            val newEmail = editTextEmail.text.toString()

            // Validar datos
            if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                // Guardar los cambios en SharedPreferences
                with(sharedPref?.edit()) {
                    this?.putString("username", newName)
                    this?.putString("email", newEmail)
                    this?.apply()
                }

                // Mostrar un mensaje de éxito
                Toast.makeText(activity, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()

                // Actualizar el Navigation Drawer y la vista de bienvenida
                updateNavigationDrawer()
            } else {
                // Mostrar mensaje de error si los campos están vacíos
                Toast.makeText(activity, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar la foto de perfil (ahora permitirá seleccionar una nueva)
        imageViewProfilePic.setOnClickListener {
            // Abrir la galería para seleccionar una imagen
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
        }
    }

    private fun updateNavigationDrawer() {
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", "")
        val email = sharedPref?.getString("email", "")
        val profilePicUri = sharedPref?.getString("profilePicUri", null)

        // Aquí, debes llamar al método del NavigationDrawer para actualizar el nombre y la imagen
        val navHeader = activity?.findViewById<NavigationView>(R.id.nav_view)?.getHeaderView(0)
        val txtName = navHeader?.findViewById<TextView>(R.id.txt_name)
        val txtEmail = navHeader?.findViewById<TextView>(R.id.txt_email)
        val imageLogo = navHeader?.findViewById<ImageView>(R.id.image_logo)

        txtName?.text = username
        txtEmail?.text = email
        profilePicUri?.let {
            val uri = Uri.parse(it)
            imageLogo?.setImageURI(uri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
            data?.data?.let { imageUri ->
                // Actualizar la imagen de perfil con la imagen seleccionada
                imageViewProfilePic.setImageURI(imageUri)

                // Opcional: Guardar la URI de la imagen en SharedPreferences si deseas mantenerla persistente
                val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    this?.putString("profilePicUri", imageUri.toString())
                    this?.apply()
                }

                // Actualizar el Navigation Drawer con la nueva foto
                updateNavigationDrawer()
            }
        }
    }
}

