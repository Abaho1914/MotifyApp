package com.abahoabbott.motify.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abahoabbott.motify.notify.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notify.CHANNEL_ID
import com.abahoabbott.motify.notify.CHANNEL_NAME
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.notify.createNotificationChannelIfNeeded
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MotifyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationManagerHelper: NotificationManagerHelper,
): CoroutineWorker(context,params){

    override suspend fun doWork(): Result {
        return try {
            //Ensure channel exists
          notificationManagerCompat.createNotificationChannelIfNeeded(
              CHANNEL_ID,
              CHANNEL_NAME,
              CHANNEL_DESCRIPTION
          )
            //Get next quote
           val quote = "Fresh out of slumber"
            //Fire notification
            notificationManagerHelper.showMotivationNotification(
                "Your Daily Motivation",
                quote
            )

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }

    }
}