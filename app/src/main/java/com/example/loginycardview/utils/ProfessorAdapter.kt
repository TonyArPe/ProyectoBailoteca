package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.domain.Professor

class ProfessorAdapter(private var professorList: MutableList<Professor>) :
    RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {


    inner class ProfessorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewProfessor)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewSpecialty: TextView = itemView.findViewById(R.id.textViewSpecialty)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)

        fun bind(professor: Professor) {
            imageView.setImageResource(professor.imageResId)
            textViewName.text = professor.name
            textViewSpecialty.text = professor.specialty
            textViewDescription.text = professor.description
            textViewEmail.text = professor.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_professor, parent, false)
        return ProfessorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        holder.bind(professorList[position])
    }

    override fun getItemCount(): Int = professorList.size

    fun updateData(newProfessors: List<Professor>) {
        professorList = newProfessors.toMutableList() // Reemplaza la lista completamente
        notifyDataSetChanged()
    }
}

