package com.abahoabbott.motify.notif

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannelRequest(context: Context){
    val importance  = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        importance
    )
        .apply {
            description = CHANNEL_DESCRIPTION
        }
    val notificationManager :NotificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager
    notificationManager.createNotificationChannel(channel)


}