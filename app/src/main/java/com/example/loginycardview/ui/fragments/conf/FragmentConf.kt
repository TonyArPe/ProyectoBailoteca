package com.example.loginycardview.ui.fragments.conf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import com.example.loginycardview.R

class FragmentConf : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí puedes cargar configuraciones si es necesario
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_conf, container, false)

        // Configurar eventos de los botones, por ejemplo, abrir un diálogo para cambiar el perfil o idioma
        val editProfileCard = view.findViewById<CardView>(R.id.cardEditProfile)
        editProfileCard.setOnClickListener {
            // Acción para editar perfil
        }

        val aboutAcademyCard = view.findViewById<CardView>(R.id.cardAboutAcademy)
        aboutAcademyCard.setOnClickListener {
            // Acción para ver información sobre la academia
        }

        return view
    }
}
