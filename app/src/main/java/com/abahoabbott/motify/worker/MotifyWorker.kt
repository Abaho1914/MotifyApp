package com.abahoabbott.motify.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abahoabbott.motify.motivate.QuotesRepository
import com.abahoabbott.motify.notify.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notify.CHANNEL_ID
import com.abahoabbott.motify.notify.CHANNEL_NAME
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.notify.createNotificationChannelIfNeeded
import com.abahoabbott.motify.qoutes.QuoteProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber


/**
 * A worker that sends a daily motivational quote as a notification.
 *
 * Uses [NotificationManagerHelper] to show the notification, and ensures
 * the required notification channel is created before sending.
 */
@HiltWorker
class MotifyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationManagerHelper: NotificationManagerHelper,
    private val quoteProvider: QuoteProvider,
    private val quotesRepository: QuotesRepository
) : CoroutineWorker(context, params) {


    /**
     * Ensures the notification channel exists and fires a motivational notification.
     *
     * @return [Result.success] if successful, or [Result.retry] on failure.
     */
    override suspend fun doWork(): Result {
        return try {
            //Ensure channel exists
            Timber.d("Creating notification channel")

            notificationManagerCompat.createNotificationChannelIfNeeded(
                CHANNEL_ID,
                CHANNEL_NAME,
                CHANNEL_DESCRIPTION
            )
            //Get next quote
            val quote = quoteProvider.getDailyQuote()

            quotesRepository.saveQuoteOfTheDay(quote)

            //Fire notification
            notificationManagerHelper.showMotivationNotification(
                "Your Daily Motivation",
                quote.toString()
            )

            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Failed to send motivation notification")
            Result.retry()
        }

    }
}