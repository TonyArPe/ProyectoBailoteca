package com.example.loginycardview.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.loginycardview.databinding.DialogEditProfessorBinding
import com.example.loginycardview.databinding.DialogUpdateProfessorBinding
import com.example.loginycardview.domain.Professor

class EditProfessorDialogFragment(
    private val professor: Professor,
    private val onSave: (Professor) -> Unit
) : DialogFragment() {

    private var _binding: DialogUpdateProfessorBinding? = null // ✅ Cambiado al nuevo XML
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUpdateProfessorBinding.inflate(LayoutInflater.from(context))

        binding.editTextName.setText(professor.name)
        binding.editTextSpecialty.setText(professor.specialty)
        binding.editTextDescription.setText(professor.description)
        binding.editTextEmail.setText(professor.email)

        return AlertDialog.Builder(requireContext())
            .setTitle("Editar Profesor")
            .setView(binding.root)
            .setPositiveButton("Guardar") { _, _ ->
                val updatedProfessor = professor.copy(
                    name = binding.editTextName.text.toString(),
                    specialty = binding.editTextSpecialty.text.toString(),
                    description = binding.editTextDescription.text.toString(),
                    email = binding.editTextEmail.text.toString()
                )
                onSave(updatedProfessor)
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
