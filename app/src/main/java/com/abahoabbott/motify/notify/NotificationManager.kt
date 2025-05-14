package com.abahoabbott.motify.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abahoabbott.motify.MainActivity
import com.abahoabbott.motify.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Constants for notification channel
const val CHANNEL_ID = "motify_channel_id"
const val CHANNEL_NAME = "Motify Notifications"
const val CHANNEL_DESCRIPTION = "Motivation notifications from Motify"

/**
 * Centralized manager for all notification-related operations
 */
class NotificationManagerHelper(private val context: Context) {

    /**
     * Shows a motivation notification with the given title and message
     */
    suspend fun showMotivationNotification(
        title: String,
        quote: String
    ): Result<Int> = withContext(Dispatchers.IO)
    {
        try {
            val notificationId = System.currentTimeMillis().toInt()
            val notificationManager = NotificationManagerCompat.from(context)

            //Create a Pending Intent that carries the quote extras
            val pendingIntent = createMainActivityPendingIntent(quote)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(quote)
                .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
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
    private fun createMainActivityPendingIntent(quote: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("QUOTE_FROM_NOTIFICATION", quote)
        }

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)

            // Use the appropriate flags based on Android version
            val flags =
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            getPendingIntent(0, flags)
                ?: throw IllegalStateException("Failed to create PendingIntent")
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