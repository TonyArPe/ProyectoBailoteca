package com.example.loginycardview.ui.activitys

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.loginycardview.R
import com.example.loginycardview.data.Professor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ProfessorProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_profile)

        imageView = findViewById(R.id.professorImageView)

        if (checkAndRequestPermissions()) {
            performActionRequiringPermissions()
        }
        initializeActivityComponents()
        setupActivityResultLaunchers()
        loadProfileImage()
    }

    private fun loadProfileImage() {
        val prefs = getSharedPreferences("ProfessorProfile", Context.MODE_PRIVATE)
        val imageUriString = prefs.getString("profileImageUri", null)
        imageUriString?.let {
            imageUri = Uri.parse(it)
            Glide.with(this).load(imageUri).into(imageView)
        }
    }

    private fun initializeActivityComponents() {
        val professor = intent.getParcelableExtra<Professor>("professor")
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val specialtyTextView: TextView = findViewById(R.id.specialtyTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val contactButton: Button = findViewById(R.id.contactButton)
        val backButton: Button = findViewById(R.id.backButton)
        val fabChangeImage: FloatingActionButton = findViewById(R.id.fabChangeImage)

        nameTextView.text = professor?.username
        specialtyTextView.text = "Especialidad: ${professor?.specialty}"
        descriptionTextView.text = "Descripción: ${professor?.description}"
        emailTextView.text = "Email: ${professor?.email}"

        contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(professor?.email))
                putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre clases")
                putExtra(Intent.EXTRA_TEXT, "Hola ${professor?.username}, estoy interesado en tus clases de ${professor?.specialty}.")
            }
            startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        backButton.setOnClickListener {
            finish()
        }

        fabChangeImage.setOnClickListener {
            if (checkAndRequestPermissions()) {
                showImagePickerOptions()
            }
        }
    }

    private fun setupActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri?.let {
                    Glide.with(this).load(it).into(imageView)
                    saveImageUriToPreferences(it.toString())
                }
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Glide.with(this).load(it).into(imageView)
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                saveImageUriToPreferences(uri.toString())
            }
        }
    }

    private fun saveImageUriToPreferences(uriString: String) {
        getSharedPreferences("ProfessorProfile", Context.MODE_PRIVATE).edit().apply {
            putString("profileImageUri", uriString)
            apply()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsNeeded.isNotEmpty()) {
            requestPermissions(permissionsNeeded.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            performActionRequiringPermissions()
        } else {
            Snackbar.make(imageView, "Permission Denied", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun performActionRequiringPermissions() {
        Snackbar.make(imageView, "All permissions granted!", Snackbar.LENGTH_LONG).show()
    }

    private fun showImagePickerOptions() {
        MaterialAlertDialogBuilder(this).setTitle("Select Image").setItems(arrayOf("Camera", "Gallery")) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }.show()
    }

    private fun openCamera() {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "new_profile_photo.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            cameraLauncher.launch(intent)
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }
}
