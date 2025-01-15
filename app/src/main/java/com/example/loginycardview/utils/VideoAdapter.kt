package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Video

class VideoAdapter(private val videoList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.titleTextView.text = video.title
        holder.descriptionTextView.text = video.description

        // Aquí puedes configurar el enlace del video para que se reproduzca, o agregar más funcionalidades
    }

    override fun getItemCount(): Int = videoList.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.video_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.video_description)
    }
}
