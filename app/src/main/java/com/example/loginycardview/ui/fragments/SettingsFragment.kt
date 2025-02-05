package com.example.loginycardview.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginycardview.R
import com.example.loginycardview.databinding.FragmentSettingsBinding
import com.example.loginycardview.ui.activitys.MainActivity
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var uriImagen = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val mainActivity = requireActivity() as MainActivity
        val sharedPreferences = mainActivity.getSharedPreferences("userSettings", Context.MODE_PRIVATE)

        // Recuperar el nombre de usuario y la URI desde SharedPreferences
        binding.editTextUsername.setText(sharedPreferences.getString("username", ""))

        val uriString = sharedPreferences.getString("uri", "")
        if (!uriString.isNullOrEmpty()) {
            val uri = Uri.parse(uriString)
            binding.imageViewProfile.setImageURI(uri) // Mostrar la imagen recuperada
        }

        // Obtener el header del NavigationView
        val navView = mainActivity.findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val imagePerfil = headerView.findViewById<CircleImageView>(R.id.image_perfil)
        val txtName = headerView.findViewById<TextView>(R.id.txt_name)

        // Registrar el activity result para seleccionar una imagen
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uriImagen = uri
                binding.imageViewProfile.setImageURI(uri) // Mostrar la imagen seleccionada
            } else {
                Toast.makeText(mainActivity, R.string.error_imagen_pick, Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el clic para elegir una imagen
        binding.imageViewProfile.setOnClickListener {
            val mimeType = "image/*"
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
        }

        // Guardar cambios en el perfil
        binding.buttonSaveSettings.setOnClickListener {
            var cambio = false

            if (uriImagen != Uri.EMPTY) {
                imagePerfil.setImageURI(uriImagen) // Actualizar imagen en el NavigationView
                sharedPreferences.edit().putString("uri", uriImagen.toString()).apply()
                cambio = true
            }

            val newUsername = binding.editTextUsername.text.toString()
            val currentUsername = txtName.text.toString()

            if (newUsername.isNotEmpty() && newUsername != currentUsername) {
                txtName.text = newUsername
                sharedPreferences.edit().putString("username", newUsername).apply()
                cambio = true
            }

            if (cambio) {
                Toast.makeText(mainActivity, R.string.perfil_actualizado, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mainActivity, R.string.no_cambios, Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PrincipalFragment()) // Volver al PrincipalFragment
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
