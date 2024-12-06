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
        val position: TextView = itemView.findViewById(R.id.professorPosition)
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
        holder.position.text = "Posición: ${position + 1}" // Se muestra el índice +1 (para empezar desde 1)
        holder.position.visibility = View.VISIBLE

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

        // Botón de Editar
        holder.buttonEdit.setOnClickListener {
            // Llamar a la función para editar este profesor
            showEditDialog(holder.itemView.context, position)
        }

        // Botón de Eliminar
        holder.buttonDelete.setOnClickListener {
            // Llamar a la función para eliminar este profesor
            showDeleteDialog(holder.itemView.context, position)
        }
    }

    override fun getItemCount(): Int = professors.size

    // Función para mostrar el diálogo de actualizar profesor
    private fun showEditDialog(context: Context, position: Int) {
        val professor = professors[position]
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_professor, null)

        // Rellenar los campos del diálogo con los datos actuales
        val editName = dialogView.findViewById<EditText>(R.id.editName)
        val editSpecialty = dialogView.findViewById<EditText>(R.id.editSpecialty)
        val editDescription = dialogView.findViewById<EditText>(R.id.editDescription)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val checkboxTopRated = dialogView.findViewById<CheckBox>(R.id.checkboxTopRated)

        editName.setText(professor.name)
        editSpecialty.setText(professor.specialty)
        editDescription.setText(professor.description)
        editEmail.setText(professor.email)
        checkboxTopRated.isChecked = professor.isTopRated

        val dialog = AlertDialog.Builder(context)
            .setTitle("Actualizar Profesor")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { _, _ ->
                // Actualizar los datos del profesor
                professor.name = editName.text.toString()
                professor.specialty = editSpecialty.text.toString()
                professor.description = editDescription.text.toString()
                professor.email = editEmail.text.toString()
                professor.isTopRated = checkboxTopRated.isChecked

                // Notificar al adapter que los datos han cambiado
                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    // Función para mostrar el diálogo de eliminar profesor
    private fun showDeleteDialog(context: Context, position: Int) {
        val professor = professors[position]

        val dialog = AlertDialog.Builder(context)
            .setTitle("Eliminar Profesor")
            .setMessage("¿Estás seguro de que quieres eliminar a ${professor.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Eliminar el profesor de la lista
                professors.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, professors.size)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}



