package com.example.loginycardview.data.repository

import android.util.Log
import com.example.loginycardview.domain.Video
import com.example.loginycardview.domain.VideoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : VideoRepository {

    override suspend fun getVideos(): List<Video> {
        return try {
            val snapshot = firestore.collection("videos")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Video::class.java) }
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al obtener videos: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun saveVideo(video: Video) {
        try {
            firestore.collection("videos")
                .add(video)
                .await()
            Log.d("FirestoreTest", "Video guardado correctamente: ${video.title}")
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error al guardar video: ${e.message}", e)
        }
    }
}
