package com.abahoabbott.motify.motivate

import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.room.QuoteDao
import com.abahoabbott.motify.room.QuoteEntity
import java.time.LocalDateTime
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

    suspend fun saveQuoteOfTheDay(quote: Quote) {
        val today = LocalDateTime.now().toLocalDate().toString()
        val entity = QuoteEntity(date = today, text = quote.text, author = quote.author)
        quoteDao.insertQuote(entity)
    }

}


