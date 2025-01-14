package com.example.loginycardview.ui.activitys

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.loginycardview.R
import com.example.loginycardview.ui.fragments.ModificarPerfilFragment

class ConfiguracionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        // Configurar el Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Menú Edición de Perfil"  // Título del Toolbar
            setDisplayHomeAsUpEnabled(true)  // Habilitar botón de retroceso
            setHomeAsUpIndicator(R.drawable.ic_back_arrow_black) // Icono de retroceso más visible
        }

        // Cargar el fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(ModificarPerfilFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Volver atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
