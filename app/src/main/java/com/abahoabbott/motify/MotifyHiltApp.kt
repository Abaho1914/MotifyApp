package com.abahoabbott.motify

import android.app.Application
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.abahoabbott.motify.notif.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notif.CHANNEL_ID
import com.abahoabbott.motify.notif.CHANNEL_NAME
import com.abahoabbott.motify.notif.createNotificationChannelIfNeeded
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MotifyHiltApp: Application() {
    override fun onCreate() {
        super.onCreate()
        //initialize notification channel during app startup
        initializeNotificationChannel()
    }

    private fun initializeNotificationChannel() {
        NotificationManagerCompat.from(this).createNotificationChannelIfNeeded(
            channelId = CHANNEL_ID,
            channelName = CHANNEL_NAME,
            description = CHANNEL_DESCRIPTION
        )
    }
}