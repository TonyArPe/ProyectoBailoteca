package com.example.loginycardview

import Professor
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardViewAdapter(private val professors: List<Professor>) :
    RecyclerView.Adapter<CardViewAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.professorImage)
        val name: TextView = itemView.findViewById(R.id.professorName)
        val specialty: TextView = itemView.findViewById(R.id.professorSpecialty)
        val rating: RatingBar = itemView.findViewById(R.id.professorRating)
        val contactButton: Button = itemView.findViewById(R.id.buttonContact)
        val expandButton: ImageButton = itemView.findViewById(R.id.buttonExpand)
        val expandedDetail: TextView = itemView.findViewById(R.id.expandedDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]

        // Glide para cargar la imagen
        Glide.with(holder.itemView.context)
            .load(professor.imageResId)
            .placeholder(R.drawable.professor_placeholder)
            .into(holder.image)

        holder.name.text = professor.name
        holder.specialty.text = "Especialidad: ${professor.specialty}"
        holder.rating.rating = if (professor.isTopRated) 5.0f else 3.0f

        // Manejo del botón de contacto
        holder.contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(professor.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases")
            intent.putExtra(Intent.EXTRA_TEXT, "Hola ${professor.name}, estoy interesado en tus clases de ${professor.specialty}.")
            holder.itemView.context.startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        // Expansión del ítem
        holder.expandButton.setOnClickListener {
            val isVisible = holder.expandedDetail.visibility == View.VISIBLE
            holder.expandedDetail.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }


    override fun getItemCount(): Int = professors.size
}
