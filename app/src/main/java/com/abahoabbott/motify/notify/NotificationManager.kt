package com.abahoabbott.motify.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abahoabbott.motify.MainActivity
import com.abahoabbott.motify.R
import com.abahoabbott.motify.analytics.AnalyticsManager
import com.abahoabbott.motify.data.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Constants for notification channel
const val CHANNEL_ID = "motify_channel_id"
const val CHANNEL_NAME = "Motify Notifications"
const val CHANNEL_DESCRIPTION = "Motivation notifications from Motify"

/**
 * Centralized manager for all notification-related operations
 */
class NotificationManagerHelper @Inject constructor(
    private val context: Context,
    private val analyticsManager: AnalyticsManager
) {

    /**
     * Shows a motivation notification with the given title and message
     */
    suspend fun showMotivationNotification(
        title: String,
        quote: Quote
    ): Result<Int> = withContext(Dispatchers.IO)
    {
        try {
            val notificationId = System.currentTimeMillis().toInt()
            val notificationManager = NotificationManagerCompat.from(context)

            //Create a Pending Intent that carries the quote extras
            val pendingIntent = createMainActivityPendingIntent(quote, notificationId)

            val bigText = "\"${quote.text}\"\n— ${quote.author}"
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_motivation)
                .setContentTitle("Today's Quote")
                .setContentText(quote.text)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(bigText)
                    .setSummaryText("Tap to open Motify"))
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_notification_motivation))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
            
            // Log notification sent event
            analyticsManager.logNotificationReceived(notificationId)
            
            Result.success(notificationId)
        } catch (e: SecurityException) {
            // Handle missing notification permission
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Creates a PendingIntent that opens MainActivity
     */
    private fun createMainActivityPendingIntent(quote: Quote, notificationId: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("QUOTE_FROM_NOTIFICATION", quote.toString())
        }

        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            val pendingIntent = getPendingIntent(0, flags)
                ?: throw IllegalStateException("Failed to create PendingIntent")
            // Log notification clicked event
            analyticsManager.logNotificationClicked(notificationId)
            pendingIntent
        }
    }

}

/**
 * Extension function to create notification channel if needed
 */
fun NotificationManagerCompat.createNotificationChannelIfNeeded(
    channelId: String,
    channelName: String,
    description: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    val channel = NotificationChannel(
        channelId,
        channelName,
        importance
    ).apply {
        this.description = description
    }
    createNotificationChannel(channel)
}