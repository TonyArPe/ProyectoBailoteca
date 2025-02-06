package com.example.loginycardview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.GetVideosUseCase
import com.example.loginycardview.domain.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoViewModel(private val getVideosUseCase: GetVideosUseCase) : ViewModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> get() = _videos

    fun loadVideos() {
        viewModelScope.launch {
            _videos.value = getVideosUseCase()
        }
    }
}