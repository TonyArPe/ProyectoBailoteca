package com.example.loginycardview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.usecases.GetVideosUseCase
import com.example.loginycardview.domain.usecases.SaveVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val saveVideoUseCase: SaveVideoUseCase
) : ViewModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> get() = _videos

    fun loadVideos() {
        viewModelScope.launch {
            _videos.value = getVideosUseCase()
        }
    }

    fun saveVideo(video: Video) {
        viewModelScope.launch {
            saveVideoUseCase(video)
            loadVideos()
        }
    }
}
