package com.example.loginycardview.data

import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository

class VideoRepositoryImpl : VideoRepository {
    override suspend fun getVideos(): List<Video> {
        // Aquí iría la lógica para obtener los videos desde una API o base de datos
        return emptyList()
    }

    override suspend fun saveVideo(video: Video) {
        // Aquí iría la lógica para guardar un video en la base de datos
    }
}