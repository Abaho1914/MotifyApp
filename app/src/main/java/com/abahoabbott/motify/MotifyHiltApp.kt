package com.abahoabbott.motify

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.abahoabbott.motify.notify.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notify.CHANNEL_ID
import com.abahoabbott.motify.notify.CHANNEL_NAME
import com.abahoabbott.motify.notify.createNotificationChannelIfNeeded
import com.abahoabbott.motify.worker.MotifyWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class MotifyHiltApp: Application(), Configuration.Provider{

    @Inject lateinit var workerFactory: HiltWorkerFactory

    /** Build WorkManager config to use Hilt’s WorkerFactory */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        //initialize notification channel during app startup
        initializeNotificationChannel()
        scheduleDailyQuoteWork()
    }



    private fun initializeNotificationChannel() {
        NotificationManagerCompat.from(this).createNotificationChannelIfNeeded(
            channelId = CHANNEL_ID,
            channelName = CHANNEL_NAME,
            description = CHANNEL_DESCRIPTION
        )
    }

    /** Schedule the QuoteWorker to run every 24 hrs at 8 AM */
    private fun scheduleDailyQuoteWork() {
        // compute millis until next 8:00 AM
        val now = Calendar.getInstance()
        val next8 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DATE, 1)
        }
        val initialDelay = next8.timeInMillis - now.timeInMillis

        val dailyWork = PeriodicWorkRequestBuilder<MotifyWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "daily_motivation_work",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWork
            )
    }
}