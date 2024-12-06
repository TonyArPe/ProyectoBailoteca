package com.example.loginycardview

import Professor
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardViewAdapter(private val professors: MutableList<Professor>) :
    RecyclerView.Adapter<CardViewAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.professorImage)
        val name: TextView = itemView.findViewById(R.id.professorName)
        val specialty: TextView = itemView.findViewById(R.id.professorSpecialty)
        val rating: RatingBar = itemView.findViewById(R.id.professorRating)
        val contactButton: Button = itemView.findViewById(R.id.buttonContact)
        val expandButton: ImageButton = itemView.findViewById(R.id.buttonExpand)
        val expandedDetail: TextView = itemView.findViewById(R.id.expandedDetails)
        val buttonEdit: ImageButton = itemView.findViewById(R.id.buttonEdit)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        val professor = professors[position]

        // Mostrar la posición
        holder.name.text = professor.name
        holder.specialty.text = "Especialidad: ${professor.specialty}"
        holder.rating.rating = if (professor.isTopRated) 5.0f else 3.0f

        // Glide para cargar la imagen
        Glide.with(holder.itemView.context)
            .load(professor.imageResId)
            .placeholder(R.drawable.professor_placeholder)
            .into(holder.image)

        // Mostrar la descripción cuando se expanda
        holder.expandButton.setOnClickListener {
            val isVisible = holder.expandedDetail.visibility == View.VISIBLE
            if (isVisible) {
                holder.expandedDetail.visibility = View.GONE
            } else {
                holder.expandedDetail.text = professor.description
                holder.expandedDetail.visibility = View.VISIBLE
            }
        }

        // Manejo del botón de contacto
        holder.contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(professor.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases")
            intent.putExtra(Intent.EXTRA_TEXT, "Hola ${professor.name}, estoy interesado en tus clases de ${professor.specialty}.")
            holder.itemView.context.startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        // Botón de Editar
        holder.buttonEdit.setOnClickListener {
            // Llamar a la función para editar este profesor, sin incluir la posición
            showEditDialog(holder.itemView.context, position)
        }

        // Botón de Eliminar
        holder.buttonDelete.setOnClickListener {
            // Llamar a la función para eliminar este profesor
            deleteProfessor(position)
        }
    }

    override fun getItemCount(): Int = professors.size

    // Función para mostrar el diálogo de actualizar profesor
    private fun showEditDialog(context: Context, position: Int) {
        val professor = professors[position]
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_professor, null)
        val dialog = AlertDialog.Builder(context)
            .setTitle("Actualizar Profesor")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.editName).text.toString()
                val specialty = dialogView.findViewById<EditText>(R.id.editSpecialty).text.toString()
                val description = dialogView.findViewById<EditText>(R.id.editDescription).text.toString()
                val email = dialogView.findViewById<EditText>(R.id.editEmail).text.toString()
                val isTopRated = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated).isChecked

                professors[position] = Professor(
                    professor.imageResId,
                    name,
                    specialty,
                    isTopRated,
                    description,
                    email
                )
                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    // Función para eliminar un profesor
    private fun deleteProfessor(position: Int) {
        professors.removeAt(position)
        notifyItemRemoved(position)
    }
}




