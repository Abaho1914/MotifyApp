package com.abahoabbott.motify.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.abahoabbott.motify.notify.NotificationManagerHelper
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

}


