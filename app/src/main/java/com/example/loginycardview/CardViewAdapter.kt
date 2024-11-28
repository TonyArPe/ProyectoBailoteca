package com.example.loginycardview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
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
            .placeholder(R.drawable.professor_placeholder) // Placeholder mientras carga
            .into(holder.image)

        holder.name.text = professor.name
        holder.specialty.text = "Especialidad: ${professor.specialty}"
        holder.rating.rating = if (professor.isTopRated) 5.0f else 3.0f

        holder.contactButton.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Contactando a ${professor.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = professors.size
}
