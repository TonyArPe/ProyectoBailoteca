package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginycardview.R
import com.example.loginycardview.data.Video
import com.example.loginycardview.utils.VideoAdapter

class GenericListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<Video>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generic_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        videoAdapter = VideoAdapter(videoList)
        recyclerView.adapter = videoAdapter

        // Aquí agregarías los videos de ejemplo o cargarías de la base de datos
        videoList.addAll(getSampleVideos())
        videoAdapter.notifyDataSetChanged()

        return view
    }

    // Método para obtener una lista de videos de ejemplo
    private fun getSampleVideos(): List<Video> {
        return listOf(
            Video("Video 1", "https://link1.com", "Descripción del video 1"),
            Video("Video 2", "https://link2.com", "Descripción del video 2"),
            // Agregar más videos según sea necesario
        )
    }
}
