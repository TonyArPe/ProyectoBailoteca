package com.example.loginycardview.domain

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
