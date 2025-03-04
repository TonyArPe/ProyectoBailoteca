package com.example.loginycardview.data.repository

import android.util.Log
import com.example.loginycardview.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerUser(user: User, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            result.user?.let {
                val userWithId = user.copy(id = it.uid)
                firestore.collection("users").document(it.uid).set(userWithId).await()
                it.sendEmailVerification().await()
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("Auth", "Error en registro: ${e.message}")
            false
        }
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.isEmailVerified ?: false
        } catch (e: Exception) {
            Log.e("Auth", "Error en login: ${e.message}")
            false
        }
    }

    suspend fun loginAsGuest(): Boolean {
        return try {
            val result = auth.signInAnonymously().await()
            result.user != null
        } catch (e: Exception) {
            Log.e("Auth", "Error en login anónimo: ${e.message}")
            false
        }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "Usuario",
            email = firebaseUser.email ?: "",
            profileImageUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("Auth", "Error al restablecer contraseña: ${e.message}")
            false
        }
    }

}
