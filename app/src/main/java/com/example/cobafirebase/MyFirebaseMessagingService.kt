// File: MyFirebaseMessagingService.kt
package com.example.cobafirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // Handle data payload
            // Misalnya, Anda dapat menangani payload data di sini
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

            // Show notification
            showNotification(it.title ?: "Title", it.body ?: "Body")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "Channel_ID"
        val channelName = "Channel_Name"
        val notificationId = 123 // Nomor unik untuk notifikasi

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.senyum_mengsedih) // Icon notifikasi kecil
            .setAutoCancel(true) // Otomatis menutup notifikasi setelah di-klik

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Membuat channel notifikasi untuk Android Oreo (API level 26) ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel description"
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Menampilkan notifikasi
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
