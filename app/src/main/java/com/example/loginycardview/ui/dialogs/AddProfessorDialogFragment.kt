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

        // 🔹 Selección de imagen de la galería
        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Añadir Profesor")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                saveProfessor(nameField, specialtyField, descriptionField, emailField, topRatedCheckbox)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    // 🔹 Manejar el resultado de la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == android.app.Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                imageView.setImageURI(uri) // ✅ Muestra la imagen seleccionada
            }
        }
    }

    private fun saveProfessor(
        nameField: EditText,
        specialtyField: EditText,
        descriptionField: EditText,
        emailField: EditText,
        topRatedCheckbox: CheckBox
    ) {
        val name = nameField.text.toString().trim()
        val specialty = specialtyField.text.toString().trim()
        val description = descriptionField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val isTopRated = topRatedCheckbox.isChecked

        if (name.isEmpty() || specialty.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Rellene los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri != null) {
            professorViewModel.uploadImageToStorage(selectedImageUri!!, { imageUrl ->
                saveProfessorToFirestore(name, specialty, description, email, isTopRated, imageUrl)
            }, { exception ->
                Toast.makeText(requireContext(), "Error al subir imagen", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseStorage", "Error al subir la imagen", exception)
            })
        } else {
            saveProfessorToFirestore(name, specialty, description, email, isTopRated, "")
        }
    }

    private fun saveProfessorToFirestore(
        name: String,
        specialty: String,
        description: String,
        email: String,
        isTopRated: Boolean,
        imageUrl: String
    ) {
        val professor = Professor(
            id = "", // Firestore asignará ID automáticamente
            imageUrl = imageUrl, // Puede estar vacío si no hay imagen
            name = name,
            specialty = specialty,
            isTopRated = isTopRated,
            description = description,
            email = email
        )
        professorViewModel.saveProfessor(professor) // 🔹 Guardar en Firestore
        Toast.makeText(requireContext(), "Profesor agregado", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
