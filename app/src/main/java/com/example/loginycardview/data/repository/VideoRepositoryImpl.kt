package com.example.loginycardview.data.repository

import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor() : VideoRepository {

    private val videoList = mutableListOf<Video>()

    override suspend fun getVideos(): List<Video> {
        return videoList
    }

    override suspend fun saveVideo(video: Video) {
        videoList.add(video)
    }
}
