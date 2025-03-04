package com.example.loginycardview.domain.usecases

import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(): List<Video> = videoRepository.getVideos()
}
