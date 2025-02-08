package com.example.loginycardview.utils

import android.animation.ObjectAnimator
import com.example.loginycardview.data.Professor
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.example.loginycardview.ui.activitys.ProfessorProfileActivity

class CardViewAdapter(private val professors: MutableList<Professor>) :
    RecyclerView.Adapter<CardViewAdapter.ProfessorViewHolder>() {

    class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.professorImage)
        val name: TextView = itemView.findViewById(R.id.professorName)
        val specialty: TextView = itemView.findViewById(R.id.professorSpecialty)
        val rating: RatingBar = itemView.findViewById(R.id.professorRating)
        val contactButton: Button = itemView.findViewById(R.id.buttonContact)
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

        holder.name.text = professor.username
        holder.specialty.text = "Especialidad: ${professor.specialty}"
        holder.rating.rating = professor.rating

        // En el método onBindViewHolder, asegurémonos de que Glide reciba el recurso de imagen correctamente
        Glide.with(holder.itemView.context)
            .load(professor.imageResId)
            .placeholder(R.drawable.professor_placeholder)
            .error(R.drawable.professor_placeholder)
            .override(500, 500) // Redimensionar la imagen a un tamaño fijo (500x500 píxeles, por ejemplo)
            .into(holder.image)



        // Hacer clic sobre la imagen para mostrar el zoom
        holder.image.setOnClickListener {
            showImageZoomDialog(holder.itemView.context, professor.imageResId)
            Log.d("CardViewAdapter", "Imagen de profesor: ${professor.imageResId}")

        }

        // Hacer el ítem completo clickeable para abrir la actividad de detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProfessorProfileActivity::class.java)
            intent.putExtra("professor", professor)
            holder.itemView.context.startActivity(intent)
        }

        // Habilitar el RatingBar para que el usuario pueda votar
        holder.rating.setOnRatingBarChangeListener { _, rating, _ ->
            professor.rating = rating
            Toast.makeText(holder.itemView.context, "Has votado con $rating estrellas", Toast.LENGTH_SHORT).show()
        }

        // Botón de contacto
        holder.contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(professor.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases")
            intent.putExtra(Intent.EXTRA_TEXT, "Hola ${professor.username}, estoy interesado en tus clases de ${professor.specialty}.")
            holder.itemView.context.startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        // Botón de Editar (visible solo si no es invitado)
        if (!isGuest(holder.itemView.context)) {
            holder.buttonEdit.setOnClickListener {
                showEditDialog(holder.itemView.context, position)
            }
        } else {
            holder.buttonEdit.visibility = View.GONE
        }

        // Botón de Eliminar (visible solo si no es invitado)
        if (!isGuest(holder.itemView.context)) {
            holder.buttonDelete.setOnClickListener {
                showDeleteConfirmationDialog(holder.itemView.context, position)
            }
        } else {
            holder.buttonDelete.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = professors.size

    private fun isGuest(context: Context): Boolean {
        return context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getBoolean("isGuest", false)
    }

    private fun showImageZoomDialog(context: Context, imageResId: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_zoom, null)
        val imageView: ImageView = dialogView.findViewById(R.id.zoomedImage)

        val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.start()

        // Cargar la imagen en el ImageView
        Glide.with(context)
            .load(imageResId) // Usar el ID del recurso para cargar la imagen
            .into(imageView)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Mostrar el diálogo
        dialog.show()

        // Hacer que el diálogo se cierre cuando se toque la imagen
        imageView.setOnClickListener {
            dialog.dismiss() // Cerrar el diálogo cuando la imagen sea tocada
        }
    }

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
                    email,
                )
                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun showDeleteConfirmationDialog(context: Context, position: Int) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Eliminar Profesor")
            .setMessage("¿Estás seguro de que deseas eliminar este profesor?")
            .setPositiveButton("Sí") { _, _ ->
                professors.removeAt(position)
                notifyItemRemoved(position)
            }
            .setNegativeButton("No", null)
            .create()
        dialog.show()
    }
}
