package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.databinding.ItemProfessorBinding
import com.example.loginycardview.domain.Professor

class ProfessorAdapter : RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder>() {

    private val professorList = mutableListOf<Professor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessorViewHolder {
        val binding = ItemProfessorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfessorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfessorViewHolder, position: Int) {
        holder.bind(professorList[position])
    }

    override fun getItemCount(): Int = professorList.size

    fun updateProfessors(newProfessors: List<Professor>) {
        professorList.clear()
        professorList.addAll(newProfessors)
        notifyDataSetChanged()
    }

    class ProfessorViewHolder(private val binding: ItemProfessorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(professor: Professor) {
            binding.imageViewProfessor.setImageResource(professor.imageResId)
            binding.textViewName.text = professor.name
            binding.textViewSpecialty.text = professor.specialty
            binding.textViewDescription.text = professor.description
            binding.textViewEmail.text = professor.email
        }
    }
}
