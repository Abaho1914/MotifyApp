package com.abahoabbott.motify.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abahoabbott.motify.data.GeminiService
import com.abahoabbott.motify.notify.CHANNEL_DESCRIPTION
import com.abahoabbott.motify.notify.CHANNEL_ID
import com.abahoabbott.motify.notify.CHANNEL_NAME
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.notify.createNotificationChannelIfNeeded
import com.abahoabbott.motify.repository.GeminiQuotesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


@HiltWorker
class GeminiQuoteWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val geminiService: GeminiService,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationManagerHelper: NotificationManagerHelper,
    private val geminiQuotesRepository: GeminiQuotesRepository
) : CoroutineWorker(
    appContext,
    params
) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Timber.i("Running Gemini Worker")
                val serviceResult = geminiService.generateQuote()

                serviceResult.fold(
                    onSuccess = { quote ->
                        //Creating notification Channel
                        notificationManagerCompat.createNotificationChannelIfNeeded(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            CHANNEL_DESCRIPTION
                        )
                        notificationManagerHelper.showMotivationNotification(
                            title = "Daily Motivation",
                            quote = quote
                        )

                        geminiQuotesRepository.saveQuoteOfTheDay(quote)
                        Timber.i("Gemini Worker Succeded")
                        Result.success()

                    },
                    onFailure = { throwable ->
                        Timber.i("Gemini Worker Failed because of ${throwable.message}")
                        Timber.w(throwable)
                        Result.retry()
                    }
                )

            } catch (throwable: Throwable) {
                Timber.i("Gemini Worker Failed because of ${throwable.toString()}")
                Timber.e(throwable)
                Result.failure()
            }
        }
    }

}