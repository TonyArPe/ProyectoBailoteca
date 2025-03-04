package com.example.loginycardview.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.databinding.ItemVideoBinding
import com.example.loginycardview.domain.Video

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val videoList = mutableListOf<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size

    fun updateVideos(newVideos: List<Video>) {
        videoList.clear()
        videoList.addAll(newVideos)
        notifyDataSetChanged()
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            binding.textViewTitle.text = video.title
            binding.textViewDescription.text = video.description
        }
    }
}
