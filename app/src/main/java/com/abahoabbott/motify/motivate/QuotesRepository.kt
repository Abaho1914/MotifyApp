package com.abahoabbott.motify.motivate

import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.room.QuoteDao
import com.abahoabbott.motify.room.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
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

    fun getTodayQuote(): Flow<Quote?> {
        val today = LocalDateTime.now().toLocalDate().toString()
        Timber.i("Time used to retrieve quote from db $today")
        return quoteDao.getQuoteByDate(today).map { it?.toQuote() }
    }

    suspend fun saveQuoteOfTheDay(quote: Quote) {
        val today = LocalDateTime.now().toLocalDate().toString()
        Timber.i("Time used save quote to db $today")
        val entity = QuoteEntity(date = today, text = quote.text, author = quote.author)
        Timber.i("Saved $entity to database")
        quoteDao.insertQuote(entity)
    }

    fun getAllSavedQuotes(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { list -> list.map { it.toQuote() } }
    }

    fun updateQuote(quote: Quote){
      //  val quoteEntity  = quote.toQuoteEntity()
        Timber.d("Updating quote: $quote")

    }



    private fun QuoteEntity.toQuote(): Quote = Quote(text, author, date,isSaved = isBookmarked)


}

