package com.example.loginycardview.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.loginycardview.R
import com.example.loginycardview.databinding.FragmentSettingsBinding
import com.example.loginycardview.ui.activitys.MainActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import java.io.InputStream

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var uriImagen = Uri.EMPTY

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            uriImagen = uri
            mostrarImagenDesdeUri(uri)  // Método corregido para mostrar la imagen sin error
        } else {
            Toast.makeText(requireContext(), R.string.error_imagen_pick, Toast.LENGTH_SHORT).show()
        }
    }

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
            mostrarImagenDesdeUri(uri) // Mostrar la imagen guardada sin errores
        }

        // Obtener el header del NavigationView
        val navView = mainActivity.findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val imagePerfil = headerView.findViewById<CircleImageView>(R.id.image_perfil)
        val txtName = headerView.findViewById<TextView>(R.id.txt_name)

        // Configurar el clic para elegir una imagen
        binding.imageViewProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE)
                } else {
                    lanzarPickerImagen()
                }
            } else {
                lanzarPickerImagen()
            }
        }

        // Guardar cambios en el perfil
        binding.buttonSaveSettings.setOnClickListener {
            var cambio = false
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val firestore = FirebaseFirestore.getInstance()
                val userRef = firestore.collection("users").document(user.uid)

                val newUsername = binding.editTextUsername.text.toString()
                val profileImageUri = uriImagen.toString()

                val updates = mutableMapOf<String, Any>()
                if (newUsername.isNotEmpty()) {
                    updates["username"] = newUsername
                    sharedPreferences.edit().putString("username", newUsername).apply()
                    cambio = true
                }
                if (profileImageUri.isNotEmpty()) {
                    updates["profileImageUri"] = profileImageUri
                    sharedPreferences.edit().putString("profileImageUri", profileImageUri).apply()
                    cambio = true
                }

                userRef.update(updates)
                    .addOnSuccessListener {
                        Log.d("SettingsFragment", "Datos actualizados en Firestore")
                    }
                    .addOnFailureListener { e ->
                        Log.e("SettingsFragment", "Error al actualizar Firestore", e)
                    }

                // **Actualizar Navigation Drawer**
                actualizarHeader()
            }

            if (cambio) {
                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No hay cambios", Toast.LENGTH_SHORT).show()
            }
        }





        binding.buttonBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PrincipalFragment())
                .commit()
        }

        return binding.root
    }

    private fun lanzarPickerImagen() {
        val mimeType = "image/*"
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
    }

    private fun mostrarImagenDesdeUri(uri: Uri) {
        try {
            val contentResolver = requireContext().contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imageViewProfile.setImageBitmap(bitmap)
            inputStream?.close()

            // Guardar la URI en SharedPreferences
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("profileImageUri", uri.toString()).apply()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


    private fun actualizarHeader() {
        val mainActivity = requireActivity() as MainActivity
        val sharedPreferences = mainActivity.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val navView = mainActivity.findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val imagePerfil = headerView.findViewById<CircleImageView>(R.id.image_perfil)
        val txtName = headerView.findViewById<TextView>(R.id.txt_name)
        val txtEmail = headerView.findViewById<TextView>(R.id.txt_email)

        val username = sharedPreferences.getString("username", "Usuario")
        val email = sharedPreferences.getString("email", FirebaseAuth.getInstance().currentUser?.email ?: "ejemplo@gmail.com")
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)

        txtName.text = username
        txtEmail.text = email

        if (profileImageUri != null) {
            imagePerfil.setImageURI(Uri.parse(profileImageUri))
        } else {
            imagePerfil.setImageResource(R.drawable.ic_placeholder)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lanzarPickerImagen()
        } else {
            Toast.makeText(requireContext(), "Permiso denegado para acceder a imágenes", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE = 1001
    }
}
