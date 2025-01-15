package com.example.loginycardview.ui.activitys

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.loginycardview.R
import com.example.loginycardview.ui.fragments.PrincipalFragment
import com.example.loginycardview.ui.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.view.GravityCompat
import com.example.loginycardview.ui.fragments.EventFragment
import com.example.loginycardview.ui.fragments.GenericListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var imageView: ImageView

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        toolbar = findViewById(R.id.tool_bar)
        imageView = findViewById(R.id.imageView)  // Asegúrate de que el ID de tu ImageView sea correcto

        // Configurar Toolbar como ActionBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.title = "La Bailoteca"

        // Cargar el fragmento principal al inicio solo si no se ha guardado el estado
        if (savedInstanceState == null) {
            replaceFragment(PrincipalFragment())
        }

        // Configuración del Navigation Drawer
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_instagram -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/labailoteca/"))
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, ConfiguracionActivity::class.java))
                }
                R.id.nav_logout -> {
                    // Realiza el logout y regresa al login
                    val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        clear()  // Limpiar preferencias
                        apply()
                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.nav_select_image -> {
                    // Solicitar permisos si no están concedidos
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
                    } else {
                        // Si el permiso está concedido, abrir la galería
                        openGallery()
                    }
                }

                R.id.nav_anuncios -> replaceFragment(EventFragment())

                R.id.nav_generic_list -> replaceFragment(GenericListFragment())
                else -> {
                    Snackbar.make(drawerLayout, "Función no implementada", Snackbar.LENGTH_SHORT).show()
                }
            }


            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Configuración del Bottom Navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(PrincipalFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                else -> false
            }
        }

        // Manejar el ícono de menú en la toolbar
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)  // Permite ir atrás si se navega entre fragmentos
        fragmentTransaction.commit()
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)  // Cerrar el Drawer si está abierto
        } else {
            super.onBackPressed()  // Manejar la navegación hacia atrás normalmente
        }
    }

    // Método para abrir la galería y seleccionar una imagen
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Maneja el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImageUri: Uri? = data?.data
            val imageBitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            imageView.setImageBitmap(imageBitmap)
        }
    }

    // Manejo de los permisos al recibir la respuesta
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abre la galería
                openGallery()
            } else {
                // Permiso denegado, puedes mostrar un mensaje al usuario
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
