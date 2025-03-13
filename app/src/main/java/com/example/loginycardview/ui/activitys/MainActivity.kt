package com.example.loginycardview.ui.activitys

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.loginycardview.R
import com.example.loginycardview.presentation.viewmodel.AuthViewModel
import com.example.loginycardview.ui.fragments.EventFragment
import com.example.loginycardview.ui.fragments.PrincipalFragment
import com.example.loginycardview.ui.fragments.SettingsFragment
import com.example.loginycardview.ui.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPref: SharedPreferences
    private lateinit var firestore: FirebaseFirestore

    private val authViewModel: AuthViewModel by viewModels()
    private var isGuestUser = false // 🔹 Variable para saber si es invitado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        firestore = FirebaseFirestore.getInstance() // Inicializar Firestore
        // 🔹 CORRECCIÓN: Ahora se asigna correctamente a la variable global.
        this.isGuestUser = intent.getBooleanExtra("isGuestUser", false)

        checkUserMode() // 🔹 Verificamos si el usuario es invitado

        // Verificar si el usuario está autenticado
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        initViews()
        insertDefaultProfessors() // Llamamos la función aquí para cargar los profesores si no existen

        lifecycleScope.launch {
            authViewModel.currentUser.collectLatest { user ->
                if (user == null) {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        setupUserData()
        setupToolbar()
        setupNavigationDrawer()
        observeAuthState()

        if (savedInstanceState == null) {
            replaceFragment(PrincipalFragment.newInstance(isGuestUser)) // 🔹 Pasamos el modo invitado
        }
    }


    private fun insertDefaultProfessors() {
        firestore.collection("professors").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                val defaultProfessors = listOf(
                    mapOf(
                        "name" to "Juan Pérez",
                        "specialty" to "Salsa",
                        "isTopRated" to true,
                        "description" to "Instructor profesional de salsa con más de 10 años de experiencia en escenarios internacionales.",
                        "email" to "juan.perez@example.com"
                    ),
                    mapOf(
                        "name" to "Ana Gómez",
                        "specialty" to "Bachata",
                        "isTopRated" to false,
                        "description" to "Especialista en bachata moderna y tradicional, con un enfoque en la musicalidad y la técnica.",
                        "email" to "ana.gomez@example.com"
                    ),
                    mapOf(
                        "name" to "Carlos López",
                        "specialty" to "Flamenco",
                        "isTopRated" to true,
                        "description" to "Bailarín flamenco reconocido internacionalmente, con una amplia trayectoria en festivales de flamenco.",
                        "email" to "carlos.lopez@example.com"
                    ),
                    mapOf(
                        "name" to "María García",
                        "specialty" to "Tango",
                        "isTopRated" to false,
                        "description" to "Profesora de tango argentino con experiencia en competiciones y espectáculos internacionales.",
                        "email" to "maria.garcia@example.com"
                    ),
                    mapOf(
                        "name" to "Luis Martínez",
                        "specialty" to "Ballet",
                        "isTopRated" to true,
                        "description" to "Coreógrafo y maestro de ballet clásico, con un enfoque en la técnica y el desarrollo artístico.",
                        "email" to "luis.martinez@example.com"
                    ),
                    mapOf(
                        "name" to "Isabel Ruiz",
                        "specialty" to "Contemporáneo",
                        "isTopRated" to true,
                        "description" to "Especialista en danza contemporánea, con una gran experiencia en improvisación y coreografía experimental.",
                        "email" to "isabel.ruiz@example.com"
                    ),
                    mapOf(
                        "name" to "Miguel Sánchez",
                        "specialty" to "Hip Hop",
                        "isTopRated" to false,
                        "description" to "Bailarín y coreógrafo de hip hop con más de 5 años enseñando en academias y campeonatos.",
                        "email" to "miguel.sanchez@example.com"
                    ),
                    mapOf(
                        "name" to "Daniel López",
                        "specialty" to "Kizomba",
                        "isTopRated" to true,
                        "description" to "Instructor de kizomba con experiencia en las mejores escuelas de baile y festivales de kizomba.",
                        "email" to "daniel.lopez@example.com"
                    )
                )

                defaultProfessors.forEach { professor ->
                    firestore.collection("professors").add(professor)
                }
                Log.d("MainActivity", "Profesores predeterminados insertados correctamente.")
            } else {
                Log.d("MainActivity", "Ya existen profesores en Firestore, no se insertan nuevamente.")
            }
        }.addOnFailureListener { e ->
            Log.e("MainActivity", "Error al verificar los profesores en Firestore", e)
        }
    }

    /**
     * 🔹 Verifica si el usuario está autenticado o es invitado y ajusta UI en consecuencia
     */
    private fun checkUserMode() {
        val user = FirebaseAuth.getInstance().currentUser
        isGuestUser = user == null || user.isAnonymous // 🔹 Verifica si el usuario es anónimo

        Log.d("MainActivity", "isGuestUser: $isGuestUser")
        Log.d("MainActivity", "User ID: ${user?.uid}") // 🔹 Ver qué UID tiene el usuario actual
    }


    private fun setupUserData() {
        val headerView = navView.getHeaderView(0)
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_perfil)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Usuario")
        val email = sharedPreferences.getString("email", "ejemplo@gmail.com")
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)

        txtNameHeader.text = username
        txtEmailHeader.text = email

        if (profileImageUri != null) {
            imageLogoHeader.setImageURI(Uri.parse(profileImageUri))
        } else {
            imageLogoHeader.setImageResource(R.mipmap.ic_launcher_foreground)
        }
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        toolbar = findViewById(R.id.tool_bar)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            title = "La Bailoteca"
        }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }

    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_instagram -> openInstagram()
                R.id.nav_settings -> if (!isGuestUser) openSettings() // 🔹 Solo si está logueado
                R.id.nav_logout -> logoutUser()
                R.id.nav_anuncios -> if (!isGuestUser) replaceFragment(EventFragment()) // 🔹 Solo si está logueado
                R.id.nav_generic_list -> replaceFragment(VideoFragment())
                R.id.nav_home -> replaceFragment(PrincipalFragment.newInstance(isGuestUser))
                else -> showSnackbar("Función no implementada")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // 🔹 Ocultar opciones si es invitado
        navView.menu.findItem(R.id.nav_settings).isVisible = !isGuestUser
        navView.menu.findItem(R.id.nav_anuncios).isVisible = !isGuestUser
        navView.menu.findItem(R.id.action_edit_item).isVisible = false // 🔹 Eliminar opción innecesaria
    }

    private fun observeAuthState() {
        val headerView = navView.getHeaderView(0)
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_perfil)

        lifecycleScope.launch {
            authViewModel.isLoggedIn.collectLatest { isLoggedIn ->
                val username = sharedPref.getString("username", "Usuario")
                val email = sharedPref.getString("email", "ejemplo@gmail.com")
                val profileImageUri = sharedPref.getString("profileImageUri", null)

                txtNameHeader.text = username
                txtEmailHeader.text = email

                if (profileImageUri != null) {
                    imageLogoHeader.setImageURI(Uri.parse(profileImageUri))
                } else {
                    imageLogoHeader.setImageResource(R.mipmap.ic_launcher_foreground)
                }

                navView.menu.findItem(R.id.nav_settings).isVisible = isLoggedIn
                navView.menu.findItem(R.id.nav_anuncios).isVisible = isLoggedIn
                navView.menu.findItem(R.id.nav_generic_list).isVisible = true
            }
        }
    }

    private fun logoutUser() {
        authViewModel.logout()
        sharedPref.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun openInstagram() {
        val uri = Uri.parse("https://www.instagram.com/labailoteca/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openSettings() {
        replaceFragment(SettingsFragment())
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(drawerLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}
