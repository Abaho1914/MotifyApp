package com.abahoabbott.motify.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuoteEntity::class], version = 3, exportSchema = false)
abstract class MotifyDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}
