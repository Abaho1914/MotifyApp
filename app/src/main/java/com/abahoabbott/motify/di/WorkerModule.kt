package com.abahoabbott.motify.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.qoutes.FirebaseQuoteProvider
import com.abahoabbott.motify.qoutes.QuoteProvider
import com.google.firebase.firestore.FirebaseFirestore
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
        fun provideNotificationManagerHelper(@ApplicationContext ctx: Context) =
            NotificationManagerHelper(ctx)

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

}


