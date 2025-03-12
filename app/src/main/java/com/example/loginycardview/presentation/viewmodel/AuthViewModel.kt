package com.example.loginycardview.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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

        if (user != null) {
            loadUserData(user.uid) // 🔹 Siempre cargar datos de Firestore al iniciar sesión
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        loadUserData(user.uid) // 🔹 Cargar datos de Firestore al iniciar sesión
                        checkUserStatus()
                    }
                    onResult(true)
                } else {
                    onResult(false)
                }
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
                            saveUserData(user.uid, username, email, null)
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

    /**
     * 🔹 Guardar los datos del usuario en Firestore
     */
    fun saveUserData(userId: String, username: String, email: String, profileImageUri: String?) {
        val userData = hashMapOf(
            "username" to username,
            "email" to email,
            "profileImageUri" to (profileImageUri ?: "")
        )

        firestore.collection("users").document(userId).set(userData)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "Datos del usuario guardados en Firestore")
                saveUserToPreferences(username, email, profileImageUri)
            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Error al guardar datos en Firestore", e)
            }
    }

    /**
     * 🔹 Cargar los datos del usuario desde Firestore
     */
    fun loadUserData(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username") ?: "Usuario"
                    val email = document.getString("email") ?: "ejemplo@gmail.com"
                    val profileImageUri = document.getString("profileImageUri")

                    saveUserToPreferences(username, email, profileImageUri)
                    Log.d("AuthViewModel", "Datos del usuario cargados correctamente")
                } else {
                    Log.e("AuthViewModel", "El documento de usuario no existe en Firestore")
                }
            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Error al obtener datos del usuario", e)
            }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _currentUser.value = null
        clearUserPreferences()
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun loginAsGuest(onResult: () -> Unit) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserStatus() // 🔹 Verifica el estado del usuario después de loguearse
                    onResult()
                }
            }
    }

    /**
     * 🔹 Guardar datos en `SharedPreferences`
     */
    private fun saveUserToPreferences(username: String, email: String, profileImageUri: String?) {
        val sharedPref = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", username)
            putString("email", email)
            putString("profileImageUri", profileImageUri ?: "")
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
