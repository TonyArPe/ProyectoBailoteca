package com.example.loginycardview.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.loginycardview.R
import android.widget.Toast

class FragmentHome : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize UI elements
        val teacherCountText: TextView = view.findViewById(R.id.teacherCount)
        val activeClassesText: TextView = view.findViewById(R.id.activeClasses)
        val manageTeachersButton: Button = view.findViewById(R.id.manageTeachersButton)
        val viewScheduleButton: Button = view.findViewById(R.id.viewScheduleButton)
        val homeButton: ImageButton = view.findViewById(R.id.homeButton)

        // Set initial values
        teacherCountText.text = "8" // Example value
        activeClassesText.text = "5" // Example value

        // Set button click listeners
        manageTeachersButton.setOnClickListener {
            Toast.makeText(requireContext(), "Gestión de profesores", Toast.LENGTH_SHORT).show()
            // Navigate to Manage Teachers Fragment or Activity
        }

        viewScheduleButton.setOnClickListener {
            Toast.makeText(requireContext(), "Ver horarios", Toast.LENGTH_SHORT).show()
            // Navigate to Schedule Fragment or Activity
        }

        homeButton.setOnClickListener {
            Toast.makeText(requireContext(), "Inicio", Toast.LENGTH_SHORT).show()
            // Perform desired home action
        }

        return view
    }
}
