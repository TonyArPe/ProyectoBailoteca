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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPref: SharedPreferences

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Verificar si el usuario está autenticado
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // Si no hay usuario, redirigir a la pantalla de Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        initViews()

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
            replaceFragment(PrincipalFragment())
        }
    }

    private fun setupUserData() {
        val headerView = navView.getHeaderView(0)
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_perfil)

        // 🔹 Cargar datos desde SharedPreferences para persistencia
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

    // 🔹 Método restaurado para configurar el menú lateral (Navigation Drawer)
    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_instagram -> openInstagram()
                R.id.nav_settings -> openSettings()
                R.id.nav_logout -> logoutUser()
                R.id.nav_anuncios -> replaceFragment(EventFragment())
                R.id.nav_generic_list -> replaceFragment(VideoFragment())
                R.id.nav_home -> replaceFragment(PrincipalFragment())
                else -> showSnackbar("Función no implementada")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
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
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment?.javaClass == fragment.javaClass) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(drawerLayout, message, Snackbar.LENGTH_SHORT).show()
    }
}
