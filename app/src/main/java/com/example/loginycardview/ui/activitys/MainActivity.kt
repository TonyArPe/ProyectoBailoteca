package com.example.loginycardview.ui.activitys

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.example.loginycardview.ui.activities.ConfiguracionActivity
import com.example.loginycardview.ui.fragments.EventFragment
import com.example.loginycardview.ui.fragments.PrincipalFragment
import com.example.loginycardview.ui.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPref: SharedPreferences

    // Registrar un callback para recibir resultados de ConfiguracionActivity
    private val configuracionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Actualizar el header del Navigation Drawer
            updateNavigationHeader()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        initViews()
        setupToolbar()
        setupNavigationDrawer()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            replaceFragment(PrincipalFragment())
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.title = "La Bailoteca"

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupNavigationDrawer() {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isGuest = sharedPref.getBoolean("isGuest", false)

        // Obtener la vista del header dentro del NavigationView
        val headerView = navView.getHeaderView(0)

        // Ahora buscamos los elementos dentro del header
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_logo)

        // Obtener los datos guardados en SharedPreferences
        val userName = sharedPref.getString("username", "Usuario")
        val userEmail = sharedPref.getString("email", "ejemplo@gmail.com")
        val userProfileImageUri = sharedPref.getString("profileImageUri", null)

        // Asignar los valores a los TextView
        txtNameHeader.text = userName
        txtEmailHeader.text = userEmail

        // Cargar la imagen del perfil
        if (userProfileImageUri != null) {
            Glide.with(this)
                .load(Uri.parse(userProfileImageUri))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(imageLogoHeader)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_instagram -> openInstagram()
                R.id.nav_settings -> openSettings()
                R.id.nav_logout -> logoutUser()
                R.id.nav_anuncios -> replaceFragment(EventFragment())
                R.id.nav_generic_list -> replaceFragment(VideoFragment())
                else -> showSnackbar("Función no implementada")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Ocultar elementos si es invitado
        if (isGuest) {
            navView.menu.findItem(R.id.nav_settings).isVisible = false
            navView.menu.findItem(R.id.nav_anuncios).isVisible = false
            navView.menu.findItem(R.id.nav_generic_list).isVisible = true
        }
    }

    private fun updateNavigationHeader() {
        val headerView = navView.getHeaderView(0)
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_logo)

        // Obtener los datos actualizados de SharedPreferences
        val userName = sharedPref.getString("username", "Usuario")
        val userEmail = sharedPref.getString("email", "ejemplo@gmail.com")
        val userProfileImageUri = sharedPref.getString("profileImageUri", null)

        // Actualizar los TextView del header
        txtNameHeader.text = userName
        txtEmailHeader.text = userEmail

        // Cargar la imagen del perfil
        if (userProfileImageUri != null) {
            Glide.with(this)
                .load(Uri.parse(userProfileImageUri))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(imageLogoHeader)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(PrincipalFragment())
                    true
                }
                R.id.nav_profile -> {
                    openSettings()
                    true
                }
                else -> false
            }
        }
    }

    private fun openInstagram() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/labailoteca/"))
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(this, ConfiguracionActivity::class.java)
        configuracionResultLauncher.launch(intent)
    }

    private fun logoutUser() {
        sharedPref.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showSnackbar(message: String) {
        // Usamos el DrawerLayout como la vista principal para mostrar el Snackbar
        Snackbar.make(drawerLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}