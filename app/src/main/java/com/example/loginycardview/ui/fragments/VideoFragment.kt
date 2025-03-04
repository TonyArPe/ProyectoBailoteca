package com.example.loginycardview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginycardview.databinding.FragmentVideoBinding
import com.example.loginycardview.domain.Video
import com.example.loginycardview.presentation.viewmodel.VideoViewModel
import com.example.loginycardview.utils.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val videoViewModel: VideoViewModel by viewModels()
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        videoViewModel.loadVideos()

        binding.btnAddVideo.setOnClickListener {
            videoViewModel.saveVideo(createDummyVideo())
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter()
        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVideos.adapter = videoAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            videoViewModel.videos.collect { videos ->
                videoAdapter.updateVideos(videos)
            }
        }
    }

    private fun createDummyVideo() = Video(
        title = "Nuevo Video",
        url = "https://www.youtube.com/shorts/GYOh9EkYAuM",
        description = "Este es un video de prueba generado automáticamente."
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
