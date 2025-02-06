package com.example.loginycardview.domain

class GetVideosUseCase(private val videoRepository: VideoRepository) {
    suspend operator fun invoke(): List<Video> {
        return videoRepository.getVideos()
    }
}