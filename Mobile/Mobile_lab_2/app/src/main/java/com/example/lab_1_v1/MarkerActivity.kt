package com.example.lab_1_v1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_marker.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MarkerActivity : AppCompatActivity() {

    // Для маркера:
    // Путь до фото
    // Координаты маркера
    var currentPhotoPath: String? = null
    var currentCoord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker)


        currentPhotoPath = intent.getStringExtra("photoPath")
        currentCoord = intent.getStringExtra("coords")

        if (currentPhotoPath != null) {
            val imageView: ImageView = findViewById(R.id.imageView)
            val uriPhoto = Uri.parse(currentPhotoPath)
            imageView.setImageURI(uriPhoto)
        }

        btnCreatePhoto.setOnClickListener {
            getPermissions()
            dispatchTakePictureIntent()
        }

        btnSave.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("photoPath", currentPhotoPath)
            intent.putExtra("coords", currentCoord)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }

    // После того, как сделали фотку
    // Берем imageView
    // И задаем ему фотку
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.imageView)
            val uriPhoto = Uri.parse(currentPhotoPath)
            imageView.setImageURI(uriPhoto)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Права писать во внешнее хранилище
    private fun getPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
    }

    // Создаем активити для создания фотографии
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            val photoFile: File? = createImageFile()

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.lab_1_v1.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, 111)
            }
        }
    }

    // Создаем файл для фоточки
    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        val imageFileName = "PNG_" +
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) +
                "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".png", storageDir)

        currentPhotoPath = image.absolutePath
        return image
    }

    // Сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("photoPath", currentPhotoPath)
        outState.putString("coord", currentCoord)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentPhotoPath = savedInstanceState.getString("photoPath")
        currentCoord = savedInstanceState.getString("coord")

        if (currentPhotoPath != null) {
            val imageView: ImageView = findViewById(R.id.imageView)
            val uriPhoto = Uri.parse(currentPhotoPath)
            imageView.setImageURI(uriPhoto)
        }
    }
}
