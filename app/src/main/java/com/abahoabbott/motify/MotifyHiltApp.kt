package com.abahoabbott.motify

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.abahoabbott.motify.managers.WorkSchedulerManager
import com.abahoabbott.motify.notify.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notify.CHANNEL_ID
import com.abahoabbott.motify.notify.CHANNEL_NAME
import com.abahoabbott.motify.notify.createNotificationChannelIfNeeded
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class MotifyHiltApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workSchedulerManager: WorkSchedulerManager


    /** Build WorkManager config to use Hilt’s WorkerFactory */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("MotifyHiltApp onCreate called")

        // Initialize notification channel
        initializeNotificationChannel()

        // Schedule daily motivation work
        workSchedulerManager.scheduleDailyMotivationWork()
    }


    private fun initializeNotificationChannel() {
        NotificationManagerCompat.from(this).createNotificationChannelIfNeeded(
            channelId = CHANNEL_ID,
            channelName = CHANNEL_NAME,
            description = CHANNEL_DESCRIPTION
        )
    }

}