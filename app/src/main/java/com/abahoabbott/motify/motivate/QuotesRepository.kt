package com.abahoabbott.motify.motivate

import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.room.QuoteDao
import com.abahoabbott.motify.room.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Repository class for managing motivational quotes.
 * Provides a reactive stream for observing the latest quote.
 *
 * @constructor The class is annotated with @Inject for easy dependency injection using frameworks like Dagger or Hilt.
 */
class QuotesRepository @Inject constructor(
    private val quoteDao: QuoteDao
) {

    private fun todayDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun getTodayQuote(): Flow<Quote?> {
        return quoteDao.getQuoteByDate(todayDate()).map { it?.toQuote() }
    }


    private fun currentTimestamp(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    suspend fun saveQuoteOfTheDay(quote: Quote) {
        val entity = QuoteEntity(timestamp = currentTimestamp(), text = quote.text, author = quote.author)
        quoteDao.insertQuote(entity)
    }

    fun getAllSavedQuotes(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { list -> list.map { it.toQuote() } }
    }



    private fun QuoteEntity.toQuote(): Quote = Quote(text, author, isBookmarked)


}