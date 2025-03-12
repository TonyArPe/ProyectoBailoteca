package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.domain.Event

class EventAdapter(
    private var events: List<Event>,
    private val onEdit: (Event) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event, onEdit, onDelete)
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<Event>) {
        this.events = newEvents
        notifyDataSetChanged()
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        private val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)

        fun bind(event: Event, onEdit: (Event) -> Unit, onDelete: (String) -> Unit) {
            titleTextView.text = event.title
            dateTextView.text = event.date

            editButton.setOnClickListener { onEdit(event) }
            deleteButton.setOnClickListener { onDelete(event.id) }
        }
    }
}
