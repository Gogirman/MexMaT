package com.example.lab_3_v1

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.IBinder
import android.widget.Toast

class MyDownloadManager: Service() {

    private var downloadID: Long = 0

    // Описываем ресивер
    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Получаем ИДшник загрузки
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            // Проверяем, является ли полученный ИДшник нашей загрузкой
            if (downloadID == id) {
                Toast.makeText(this@MyDownloadManager, "Download Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete);
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val url = intent.getStringExtra("url")
        downloadFile(url)
        return START_STICKY
    }

    private fun downloadFile(url: String?) {

        val request = DownloadManager.Request(Uri.parse(url))

        request.setTitle(Uri.parse(url).lastPathSegment)
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE)
        request.setDescription("Downloading")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setDestinationInExternalFilesDir(applicationContext, "/folder", Uri.parse(url).lastPathSegment);

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadID = manager.enqueue(request)
    }

}