import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.loginycardview.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ItemModificationDialogFragment : DialogFragment() {

    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private val cameraRequestCode = 1
    private val galleryRequestCode = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_item_modification, container, false)
        imageView = view.findViewById(R.id.imageView)

        view.findViewById<Button>(R.id.buttonCamera).setOnClickListener {
            openCamera()
        }

        view.findViewById<Button>(R.id.buttonGallery).setOnClickListener {
            openGallery()
        }

        return view
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    imageUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, cameraRequestCode)
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, galleryRequestCode)
    }

    private fun createImageFile(): File {
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                cameraRequestCode -> {
                    imageView.setImageURI(imageUri)
                }
                galleryRequestCode -> {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)
                }
            }
        }
    }

    // Converts image to Base64 (to be used later)
    private fun convertToBase64(bitmap: Bitmap): String {
        ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            return Base64.encodeToString(toByteArray(), Base64.DEFAULT)
        }
    }

    // Load a Bitmap from Uri (used for Base64 conversion later)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}
