package com.example.loginycardview.utils

import android.content.Intent
import android.net.Uri
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
import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor
import com.example.loginycardview.ui.activitys.ProfessorProfileActivity

class ProfessorAdapter(
    private val onEdit: (Professor) -> Unit,
    private val onDelete: (Professor) -> Unit,
    private val isGuestUser: Boolean // 🔹 Se añade este parámetro para validar si el usuario es invitado
) : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    private val professorList = mutableListOf<Professor>()

    fun updateProfessors(newProfessors: List<Professor>) {
        professorList.clear()
        professorList.addAll(newProfessors)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
        return ProfessorViewHolder(view, isGuestUser) // 🔹 Pasamos isGuestUser al ViewHolder
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professorList[position]
        holder.bind(professor, onEdit, onDelete)
    }

    override fun getItemCount(): Int = professorList.size

    class ProfessorViewHolder(itemView: View, private val isGuestUser: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.professorImage)
        private val nameTextView: TextView = itemView.findViewById(R.id.professorName)
        private val specialtyTextView: TextView = itemView.findViewById(R.id.professorSpecialty)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.professorRating)
        private val contactButton: Button = itemView.findViewById(R.id.buttonContact)
        private val editButton: ImageView = itemView.findViewById(R.id.buttonEdit)
        private val deleteButton: ImageView = itemView.findViewById(R.id.buttonDelete)

        fun bind(professor: Professor, onEdit: (Professor) -> Unit, onDelete: (Professor) -> Unit) {
            Glide.with(itemView.context)
                .load(professor.imageUrl ?: R.drawable.ic_placeholder) // 🔹 Si `null`, usa imagen por defecto
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(imageView)

            nameTextView.text = professor.name
            specialtyTextView.text = professor.specialty
            ratingBar.rating = if (professor.isTopRated) 5.0f else 3.0f // 🔹 5 estrellas si es destacado, 3 si no

            // Botón de contacto (envío de correo)
            contactButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${professor.email}") // Se usa `mailto:` para abrir el email
                    putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases de ${professor.specialty}")
                    putExtra(Intent.EXTRA_TEXT, "Hola ${professor.name}, me gustaría obtener más información sobre tus clases de ${professor.specialty}.")
                }

                if (intent.resolveActivity(itemView.context.packageManager) != null) {
                    itemView.context.startActivity(intent)
                } else {
                    Toast.makeText(itemView.context, "No hay apps de correo disponibles", Toast.LENGTH_SHORT).show()
                }
            }

            // 🔹 **Ocultar botones si es usuario invitado**
            if (isGuestUser) {
                editButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            } else {
                // 🔹 Si es usuario normal, permitir editar y eliminar
                editButton.setOnClickListener { onEdit(professor) }
                deleteButton.setOnClickListener { onDelete(professor) }
            }

            // 📌 **Abrir perfil del profesor al hacer clic en el item**
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ProfessorProfileActivity::class.java).apply {
                    putExtra("professor", professor)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
