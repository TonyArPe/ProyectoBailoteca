package com.example.loginycardview.ui.fragments.ppal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Professor
import com.example.loginycardview.utils.CardViewAdapter
import com.google.android.material.navigation.NavigationView
import java.io.IOException

class FragmentPpal : Fragment() {

    private lateinit var professorList: MutableList<Professor>
    private lateinit var adapter: CardViewAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var profileImage: ImageView

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ppal, container, false)

        setupRecyclerView(view)
        setupDrawerLayout(view)

        return view
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        professorList = mutableListOf(
            Professor(R.drawable.professor1, "Juan Pérez", "Salsa", true, "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.", "juan.perez@example.com"),
            Professor(R.drawable.professor2, "Ana Gómez", "Bachata", false, "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.", "ana.gomez@example.com"),
            Professor(R.drawable.professor3, "Carlos López", "Flamenco", true, "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.", "carlos.lopez@example.com"),
            Professor(R.drawable.professor4, "María García", "Tango", false, "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.", "maria.garcia@example.com"),
            Professor(R.drawable.professor5, "Luis Martínez", "Ballet", true, "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.", "luis.martinez@example.com"),
            Professor(R.drawable.professor6, "Isabel Ruiz", "Contemporáneo", true, "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.", "isabel.ruiz@example.com"),
            Professor(R.drawable.professor7, "Miguel Sánchez", "Hip Hop", false, "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.", "miguel.sanchez@example.com"),
            Professor(R.drawable.professor8, "Daniel López", "Kizomba", true, "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.", "daniel.lopez@example.com")
        )

        adapter = CardViewAdapter(professorList)
        recyclerView.adapter = adapter
    }

    private fun setupDrawerLayout(view: View) {
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.navigation_view)

        val headerView = navigationView.getHeaderView(0)
        profileImage = headerView.findViewById(R.id.profile_image)
        val usernameText = headerView.findViewById<TextView>(R.id.username_text)
        val emailText = headerView.findViewById<TextView>(R.id.email_text)

        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val email = sharedPref.getString("email", "usuario@example.com")

        usernameText.text = username
        emailText.text = email

        profileImage.setOnClickListener { openGallery() }

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri? = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                profileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
