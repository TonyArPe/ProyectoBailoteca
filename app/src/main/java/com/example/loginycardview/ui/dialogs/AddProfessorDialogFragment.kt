package com.example.loginycardview.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.presentation.viewmodel.ProfessorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProfessorDialogFragment(private val onProfessorAdded: (Professor) -> Unit) : DialogFragment() {

    private val professorViewModel: ProfessorViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private lateinit var imageView: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_professor, null)

        val nameField = dialogView.findViewById<EditText>(R.id.editName)
        val specialtyField = dialogView.findViewById<EditText>(R.id.editSpecialty)
        val descriptionField = dialogView.findViewById<EditText>(R.id.editDescription)
        val emailField = dialogView.findViewById<EditText>(R.id.editEmail)
        val topRatedCheckbox = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated)
        imageView = dialogView.findViewById(R.id.imagePreview)
        val buttonSelectImage = dialogView.findViewById<Button>(R.id.buttonSelectImage)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)

        // 🔹 Permitir seleccionar una imagen opcional
        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // 🔹 Lógica para guardar el profesor
        buttonSave.setOnClickListener {
            val name = nameField.text.toString().trim()
            val specialty = specialtyField.text.toString().trim()
            val description = descriptionField.text.toString().trim()
            val email = emailField.text.toString().trim()

            if (name.isEmpty() || specialty.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Rellene todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("AddProfessorDialog", "Guardando profesor...")

            if (selectedImageUri != null) {
                // 🔹 Si hay imagen, primero la subimos a Firebase Storage
                professorViewModel.uploadImageToStorage(selectedImageUri!!, { imageUrl ->
                    Log.d("AddProfessorDialog", "Imagen subida: $imageUrl")
                    saveProfessor(name, specialty, description, email, imageUrl, topRatedCheckbox.isChecked)
                }, { exception ->
                    Toast.makeText(requireContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseStorage", "Error al subir la imagen", exception)
                })
            } else {
                // 🔹 Si NO hay imagen, guardamos el profesor con `null`
                Log.d("AddProfessorDialog", "Guardando sin imagen")
                saveProfessor(name, specialty, description, email, null, topRatedCheckbox.isChecked)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Añadir Profesor")
            .setView(dialogView)
            .setNegativeButton("Cancelar", null)
            .create()
    }

    // 🔹 Nueva función para guardar profesor (imagen opcional)
    private fun saveProfessor(name: String, specialty: String, description: String, email: String, imageUrl: String?, isTopRated: Boolean) {
        val professor = Professor(
            id = "", // Firestore asignará ID automáticamente
            imageUrl = imageUrl, // Puede ser `null`
            name = name,
            specialty = specialty,
            isTopRated = isTopRated,
            description = description,
            email = email
        )

        professorViewModel.saveProfessor(professor) // 🔹 Guardar en Firestore
        onProfessorAdded(professor) // 🔹 Notifica al fragmento principal
        Toast.makeText(requireContext(), "Profesor agregado correctamente", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    // 🔹 Manejar el resultado de la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == android.app.Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                imageView.setImageURI(uri) // ✅ Muestra la imagen seleccionada
                Log.d("AddProfessorDialog", "Imagen seleccionada: $uri")
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
