package com.example.loginycardview.domain

interface VideoRepository {
    suspend fun getVideos(): List<Video>
    suspend fun saveVideo(video: Video)
}