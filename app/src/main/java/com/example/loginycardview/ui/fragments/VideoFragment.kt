package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Video
import com.example.loginycardview.utils.VideoAdapter

class VideoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoList: List<Video>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_video, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Agregamos el video de prueba con la URL de YouTube
        videoList = listOf(
            Video(
                title = "Video de prueba",
                description = "Este es un video de prueba de YouTube",
                videoUrl = "https://www.youtube.com/shorts/GYOh9EkYAuM"
            )
        )

        val adapter = VideoAdapter(videoList)
        recyclerView.adapter = adapter

        return rootView
    }
}



