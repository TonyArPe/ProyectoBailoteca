package com.example.loginycardview.domain.usecases

import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository
import javax.inject.Inject

class SaveVideoUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(video: Video) {
        videoRepository.saveVideo(video)
    }
}
