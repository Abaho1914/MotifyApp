package com.abahoabbott.motify.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("SELECT * FROM QuotesTable WHERE date =:date LIMIT 1")
    fun getQuoteByDate(date: String): Flow<QuoteEntity?>


    @Update
    suspend fun updateQuote(quote: QuoteEntity)

    @Query("SELECT * FROM QuotesTable ORDER BY date DESC LIMIT 1")
    fun getLatestQuote(): Flow<QuoteEntity?>

    @Query("SELECT * FROM QuotesTable ORDER BY date DESC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>
}
