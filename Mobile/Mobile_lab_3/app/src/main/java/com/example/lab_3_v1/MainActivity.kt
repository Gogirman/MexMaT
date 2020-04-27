package com.example.lab_3_v1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDownload.setOnClickListener {
            // Даем права
            getPermissions()

            // Берем текст ссылки
            val textURL = findViewById(R.id.textURL) as EditText
            val url = textURL.text.toString()

            // Стартуем сервис
            val intent = Intent(this, MyDownloadManager::class.java)
            intent.putExtra("url", url)
            startService(intent)
        }
    }

    // Права писать во внешнее хранилище
    private fun getPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }
    }
}
