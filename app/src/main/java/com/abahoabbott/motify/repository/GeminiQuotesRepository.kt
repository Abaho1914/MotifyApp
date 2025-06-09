package com.abahoabbott.motify.repository

import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.room.QuoteDao
import com.abahoabbott.motify.room.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class GeminiQuotesRepository @Inject constructor(
    private val quoteDao: QuoteDao
) {
    suspend fun saveQuoteOfTheDay(quote: Quote) {
        val today = LocalDateTime.now().toLocalDate().toString()
        val entity = QuoteEntity(date = today, text = quote.text, author = quote.author)
        quoteDao.insertQuote(entity)

    }

    suspend fun updateQuote(quote: Quote) {
        Timber.d("Updating quote: $quote")
        val entity = QuoteEntity(
            id = quote.id,
            date = quote.date,
            text = quote.text,
            author = quote.author,
            isBookmarked = quote.isSaved
        )
        quoteDao.updateQuote(entity)
    }

    fun getTodayQuote(): Flow<Quote?> {
        val today = LocalDateTime.now().toLocalDate().toString()
        Timber.i("Time used to retrieve quote from db $today")
        return quoteDao.getQuoteByDate(today).map { it?.toQuote() }
    }


    private fun QuoteEntity.toQuote(): Quote = Quote(
        id = id,
        text = text,
        author = author,
        date = date,
        isSaved = isBookmarked
    )


}