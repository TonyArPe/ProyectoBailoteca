package com.example.loginycardview.domain.usecases

import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    operator fun invoke(): Flow<List<Video>> = flow {
        val videos = videoRepository.getVideos()
        emit(videos) // 🔹 Envía los videos como Flow
    }
}
