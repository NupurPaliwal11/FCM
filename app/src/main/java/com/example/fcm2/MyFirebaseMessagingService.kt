package com.example.fcm2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.fcm2"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Handle token refresh here
        // For example, you can send the token to your server for registration
        // You can also store the token locally for future use
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Extract title and body from the message
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // Show the notification
        if (title != null && body != null) {
            showNotification(applicationContext, title, body)
        }
    }

    internal fun showNotification(
        context: Context,
        title: String,
        body: String
    ) {
        // Create notification intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Generate a unique notification ID using the current timestamp
        val notificationId = System.currentTimeMillis().toInt()
        // Show the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


}