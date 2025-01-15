package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Event

class EventAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.tvTitle.text = event.title
        holder.tvDate.text = event.date
        holder.tvDescription.text = event.description
    }

    override fun getItemCount(): Int = eventList.size

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_event_title)
        val tvDate: TextView = itemView.findViewById(R.id.tv_event_date)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_event_description)
    }
}
