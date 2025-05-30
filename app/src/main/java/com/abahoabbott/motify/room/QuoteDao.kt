package com.abahoabbott.motify.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing and managing quotes in the QuotesTable.
 * Provides methods to insert, update, and query quotes by date or retrieve all quotes.
 */
@Dao
interface QuoteDao {

    /**
     * Inserts a quote into the QuotesTable. If a quote with the same primary key exists, it will be replaced.
     * @param quote The [QuoteEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    /**
     * Retrieves a quote for a specific date.
     * @param date The date of the quote to retrieve (format: String).
     * @return A [Flow] emitting the [QuoteEntity] for the given date, or null if not found.
     */
    @Query("SELECT * FROM QuotesTable WHERE date =:date LIMIT 1")
    fun getQuoteByDate(date: String): Flow<QuoteEntity?>

    /**
     * Updates an existing quote in the QuotesTable.
     * @param quote The [QuoteEntity] to update.
     */
    @Update
    suspend fun updateQuote(quote: QuoteEntity)

    /**
     * Retrieves the most recently added quote based on date.
     * @return A [Flow] emitting the latest [QuoteEntity], or null if the table is empty.
     */
    @Query("SELECT * FROM QuotesTable ORDER BY date DESC LIMIT 1")
    fun getLatestQuote(): Flow<QuoteEntity?>

    /**
     * Retrieves all quotes ordered by date in descending order.
     * @return A [Flow] emitting a list of all [QuoteEntity] objects.
     */
    @Query("SELECT * FROM QuotesTable ORDER BY date DESC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>
}
