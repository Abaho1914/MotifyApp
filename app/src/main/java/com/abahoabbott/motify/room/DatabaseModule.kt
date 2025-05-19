package com.abahoabbott.motify.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MotifyDatabase {
        return Room.databaseBuilder(
            appContext,
            MotifyDatabase::class.java,
            "motify.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideQuoteDao(db: MotifyDatabase): QuoteDao = db.quoteDao()
}
