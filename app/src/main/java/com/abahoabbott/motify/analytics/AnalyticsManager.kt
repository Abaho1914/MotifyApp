package com.abahoabbott.motify.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {
    companion object {
        // Event names
        private const val EVENT_QUOTE_VIEWED = "quote_viewed"
        private const val EVENT_NEW_QUOTE_REQUESTED = "new_quote_requested"
        private const val EVENT_NOTIFICATION_RECEIVED = "notification_received"
        private const val EVENT_NOTIFICATION_CLICKED = "notification_clicked"
        private const val EVENT_APP_OPENED = "app_opened"
        
        // Parameter names
        private const val PARAM_QUOTE_AUTHOR = "quote_author"
        private const val PARAM_QUOTE_LENGTH = "quote_length"
        private const val PARAM_NOTIFICATION_ID = "notification_id"
        private const val PARAM_SOURCE = "source"
    }

    fun logQuoteViewed(quoteAuthor: String, quoteLength: Int) {
        firebaseAnalytics.logEvent(EVENT_QUOTE_VIEWED) {
            param(PARAM_QUOTE_AUTHOR, quoteAuthor)
            param(PARAM_QUOTE_LENGTH, quoteLength.toLong())
        }
    }

    fun logNewQuoteRequested() {
        firebaseAnalytics.logEvent(EVENT_NEW_QUOTE_REQUESTED) {
            param(PARAM_SOURCE, "user_request")
        }
    }

    fun logNotificationReceived(notificationId: Int) {
        firebaseAnalytics.logEvent(EVENT_NOTIFICATION_RECEIVED) {
            param(PARAM_NOTIFICATION_ID, notificationId.toLong())
        }
    }

    fun logNotificationClicked(notificationId: Int) {
        firebaseAnalytics.logEvent(EVENT_NOTIFICATION_CLICKED) {
            param(PARAM_NOTIFICATION_ID, notificationId.toLong())
        }
    }

    fun logAppOpened() {
        firebaseAnalytics.logEvent(EVENT_APP_OPENED) {
            param(PARAM_SOURCE, "app_launch")
        }
    }
} 