package com.abahoabbott.motify.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("SELECT * FROM QuotesTable WHERE timestamp LIKE :date || '%' LIMIT 1")
    fun getQuoteByDate(date: String): Flow<QuoteEntity?>


    @Query("SELECT * FROM QuotesTable ORDER BY timestamp DESC LIMIT 1")
    fun getLatestQuote(): Flow<QuoteEntity?>

    @Query("SELECT * FROM QuotesTable ORDER BY timestamp DESC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>
}
