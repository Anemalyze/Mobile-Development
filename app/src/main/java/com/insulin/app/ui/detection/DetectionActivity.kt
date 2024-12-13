package com.insulin.app.ui.detection

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.insulin.app.databinding.ActivityDetectionBinding
import java.io.File
import java.io.IOException
import android.os.Environment

class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button untuk memilih gambar dari galeri atau mengambil foto
        binding.buttonSelectImage.setOnClickListener {
            val options = arrayOf("Pilih dari Galeri", "Ambil Foto")
            val builder = AlertDialog.Builder(this)
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> takePhotoWithCamera()
                }
            }
            builder.show()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun takePhotoWithCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile = createImageFile()
            imageUri = FileProvider.getUriForFile(
                this, "com.insulin.app.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    private fun createImageFile(): File {
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return try {
            File.createTempFile("JPEG_", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            File("")
        }
    }

    // Menangani hasil pemilihan gambar atau pengambilan foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                }
                TAKE_PHOTO_REQUEST -> {
                    binding.imageView.setImageURI(imageUri)
                }
            }
        }
    }
}
