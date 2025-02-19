package com.example.loginycardview.ui.activitys

import ItemModificationDialogFragment
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.loginycardview.R
import com.example.loginycardview.ui.fragments.EventFragment
import com.example.loginycardview.ui.fragments.PrincipalFragment
import com.example.loginycardview.ui.fragments.SettingsFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        initViews()
        setupToolbar()
        setupNavigationDrawer()

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
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            title = "La Bailoteca"
        }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        toolbar.findViewById<ImageButton>(R.id.editItemButton).setOnClickListener {
            showItemModificationDialog()
        }
    }


    private fun showItemModificationDialog() {
        ItemModificationDialogFragment().show(supportFragmentManager, "ItemModificationDialog")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_item -> {
                showItemModificationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupNavigationDrawer() {
        val isGuest = sharedPref.getBoolean("isGuest", false)
        val headerView = navView.getHeaderView(0)
        val txtNameHeader: TextView = headerView.findViewById(R.id.txt_name)
        val txtEmailHeader: TextView = headerView.findViewById(R.id.txt_email)
        val imageLogoHeader: ImageView = headerView.findViewById(R.id.image_perfil)

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

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_instagram -> openInstagram()
                R.id.nav_settings -> openSettings()
                R.id.nav_logout -> logoutUser()
                R.id.nav_anuncios -> replaceFragment(EventFragment())
                R.id.nav_generic_list -> replaceFragment(VideoFragment())
                R.id.nav_home -> {
                    replaceFragment(PrincipalFragment())
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> showSnackbar("Función no implementada")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        if (isGuest) {
            navView.menu.findItem(R.id.nav_settings).isVisible = false
            navView.menu.findItem(R.id.nav_anuncios).isVisible = false
            navView.menu.findItem(R.id.nav_generic_list).isVisible = true
        }
    }

    private fun openInstagram() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/labailoteca/")))
    }

    private fun openSettings() {
        replaceFragment(SettingsFragment())
    }

    private fun logoutUser() {
        sharedPref.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
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
