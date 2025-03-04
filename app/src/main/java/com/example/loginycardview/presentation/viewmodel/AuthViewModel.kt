package com.example.loginycardview.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    application: Application
) : AndroidViewModel(application) {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        val user = auth.currentUser
        _isLoggedIn.value = user != null && !user.isAnonymous
        _currentUser.value = user
        saveUserToPreferences(user)
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserStatus()
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
    }

    fun loginAsGuest(onResult: () -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserStatus()
                    onResult()
                }
            }
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun registerUser(email: String, password: String, username: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            checkUserStatus()
                            callback(true, null)
                        } else {
                            callback(false, "Error al enviar correo de verificación.")
                        }
                    }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _currentUser.value = null
        clearUserPreferences()
    }

    private fun saveUserToPreferences(user: FirebaseUser?) {
        val sharedPref = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", user?.displayName ?: "Usuario")
            putString("email", user?.email ?: "No registrado")
            putString("profileImageUri", user?.photoUrl?.toString() ?: "")
            putBoolean("isLoggedIn", user != null)
            apply()
        }
    }

    private fun clearUserPreferences() {
        val sharedPref = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }
}
