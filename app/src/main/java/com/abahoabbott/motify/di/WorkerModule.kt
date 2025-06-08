package com.abahoabbott.motify.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.abahoabbott.motify.analytics.AnalyticsManager
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.qoutes.FirebaseQuoteProvider
import com.abahoabbott.motify.qoutes.QuoteProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
        @Provides
        @Singleton
        fun provideNotificationManagerHelper(@ApplicationContext ctx: Context,analyticsManager: AnalyticsManager) =
            NotificationManagerHelper(ctx,analyticsManager)

    /** Provide the platform NotificationManagerCompat instance */
    @Provides
    @Singleton
    fun provideNotificationManagerCompat(
        @ApplicationContext ctx: Context
    ): NotificationManagerCompat =
        NotificationManagerCompat.from(ctx)

    /**
     * Provides the WorkManager instance
     */
    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideQuoteProvider(firestore: FirebaseFirestore): QuoteProvider {
        return FirebaseQuoteProvider(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideAnalyticsManager(firebaseAnalytics: FirebaseAnalytics): AnalyticsManager {
        return AnalyticsManager(firebaseAnalytics)
    }

}


