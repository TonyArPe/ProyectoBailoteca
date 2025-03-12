package com.example.loginycardview.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor

class AddProfessorDialogFragment(private val onProfessorAdded: (Professor) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_professor, null)

        val nameField = dialogView.findViewById<EditText>(R.id.editName)
        val specialtyField = dialogView.findViewById<EditText>(R.id.editSpecialty)
        val descriptionField = dialogView.findViewById<EditText>(R.id.editDescription)
        val emailField = dialogView.findViewById<EditText>(R.id.editEmail)
        val topRatedCheckbox = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Añadir Profesor")
            .setView(dialogView)
            .setPositiveButton("Añadir") { _, _ ->
                val name = nameField.text.toString()
                val specialty = specialtyField.text.toString()
                val description = descriptionField.text.toString()
                val email = emailField.text.toString()
                val isTopRated = topRatedCheckbox.isChecked

                if (name.isNotEmpty() && specialty.isNotEmpty() && email.isNotEmpty()) {
                    val newProfessor = Professor(
                        id = "", // Firestore asignará un ID automáticamente
                        imageUrl = "https://example.com/default_image.jpg", // 🔹 URL de imagen por defecto
                        name = name,
                        specialty = specialty,
                        isTopRated = isTopRated,
                        description = description,
                        email = email
                    )

                    onProfessorAdded(newProfessor)
                    Toast.makeText(requireContext(), "Profesor añadido exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)

        return builder.create()
    }
}
