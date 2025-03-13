package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginycardview.R
import com.example.loginycardview.databinding.FragmentPrincipalBinding
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.presentation.viewmodel.ProfessorViewModel
import com.example.loginycardview.ui.dialogs.AddProfessorDialogFragment
import com.example.loginycardview.ui.dialogs.EditProfessorDialogFragment
import com.example.loginycardview.utils.ProfessorAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrincipalFragment : Fragment(R.layout.fragment_principal) {

    private val professorViewModel: ProfessorViewModel by viewModels()
    private lateinit var professorAdapter: ProfessorAdapter
    private var _binding: FragmentPrincipalBinding? = null
    private val binding get() = _binding!!

    private var popupWindow: PopupWindow? = null // PopupWindow para el submenú FAB
    private var isGuestUser = false // 🔹 Variable para saber si es invitado

    companion object {
        fun newInstance(isGuestUser: Boolean): PrincipalFragment {
            val fragment = PrincipalFragment()
            val args = Bundle()
            args.putBoolean("isGuestUser", isGuestUser)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrincipalBinding.bind(view)

        isGuestUser = arguments?.getBoolean("isGuestUser", false) ?: false // 🔹 Obtenemos el valor de isGuestUser correctamente
        setupRecyclerView() // 🔹 Configuramos el RecyclerView
        setupFabMenu() // 🔹 Ahora el FAB tendrá la funcionalidad activada al inicio

        requireActivity().title = "La Bailoteca"

        checkUserMode() // 🔹 Verificamos si el usuario está logueado

        observeProfessors()
        professorViewModel.loadProfessors()
    }

    /**
     * Método para comprobar si el usuario está en modo invitado
     */
    private fun checkUserMode() {
        val user = FirebaseAuth.getInstance().currentUser
        isGuestUser = user == null || user.isAnonymous // 🔹 Verifica si es usuario anónimo

        Log.d("PrincipalFragment", "isGuestUser: $isGuestUser")

        if (isGuestUser) {
            binding.fabAdd.visibility = View.GONE // 🔹 Ocultamos el FAB si es invitado
        } else {
            binding.fabAdd.visibility = View.VISIBLE // 🔹 Nos aseguramos de que reaparezca si el usuario está logueado
        }
    }


    private fun setupRecyclerView() {
        professorAdapter = ProfessorAdapter(
            onEdit = { professor -> showEditProfessorDialog(professor) },
            onDelete = { professor -> professorViewModel.deleteProfessor(professor.id) },
            isGuestUser = isGuestUser // 🔹 Pasamos el estado correctamente al adapter
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = professorAdapter
    }

    private fun observeProfessors() {
        viewLifecycleOwner.lifecycleScope.launch {
            professorViewModel.professors.collect { professors ->
                if (professors.isNotEmpty()) {
                    professorAdapter.updateProfessors(professors)
                } else {
                    Toast.makeText(requireContext(), "No se encontraron profesores", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupFabMenu() {
        if (isGuestUser) {
            binding.fabAdd.visibility = View.GONE // 🔹 Ocultamos el FAB si es invitado
            return
        }

        binding.fabAdd.visibility = View.VISIBLE // 🔹 Aseguramos que reaparezca si el usuario está logueado

        binding.fabAdd.setOnClickListener {
            showFabMenu() // ✅ Esto debe ejecutarse cuando se hace clic
        }
    }


    private fun showFabMenu() {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
            return
        }

        val popupView = layoutInflater.inflate(R.layout.layout_bottom_navigation, null)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 10f
            isFocusable = true
            isOutsideTouchable = true
        }

        val addProfessorOption: TextView = popupView.findViewById(R.id.add_professor_option)
        val scheduleClassOption: TextView = popupView.findViewById(R.id.schedule_class_option)

        addProfessorOption.setOnClickListener {
            popupWindow?.dismiss()
            showAddProfessorDialog()
        }

        scheduleClassOption.setOnClickListener {
            popupWindow?.dismiss()
        }

        popupWindow?.showAtLocation(binding.fabAdd, Gravity.NO_GRAVITY, binding.fabAdd.x.toInt(), (binding.fabAdd.y - 250).toInt())
    }


    private fun showAddProfessorDialog() {
        val addDialog = AddProfessorDialogFragment { newProfessor ->
            val professor = Professor(
                id = "",
                imageUrl = newProfessor.imageUrl.takeIf { !it.isNullOrBlank() }, // 🔹 Guarda `null` si está vacío                name = newProfessor.name,
                specialty = newProfessor.specialty,
                isTopRated = newProfessor.isTopRated,
                description = newProfessor.description,
                email = newProfessor.email
            )
            professorViewModel.saveProfessor(professor)
            Toast.makeText(requireContext(), "Profesor agregado", Toast.LENGTH_SHORT).show()
        }
        addDialog.show(parentFragmentManager, "AddProfessorDialog")
    }

    private fun showEditProfessorDialog(professor: Professor) {
        val editDialog = EditProfessorDialogFragment(professor) { updatedProfessor ->
            professorViewModel.saveProfessor(updatedProfessor)
            Toast.makeText(requireContext(), "Profesor actualizado", Toast.LENGTH_SHORT).show()
        }
        editDialog.show(parentFragmentManager, "EditProfessorDialog")
    }

    private fun deleteProfessor(professor: Professor) {
        professorViewModel.deleteProfessor(professor.id)
        Toast.makeText(requireContext(), "Profesor eliminado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
