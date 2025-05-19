package com.abahoabbott.motify.motivate

import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.room.QuoteDao
import com.abahoabbott.motify.room.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
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

    val motivationQuotes = listOf(
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
    )

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    suspend fun saveQuoteOfTheDay(quote: Quote) {
        val entity = QuoteEntity(date = today(), text = quote.text, author = quote.author)
        quoteDao.insertQuote(entity)
    }

    fun getAllSavedQuotes(): Flow<List<Quote>> {
        return quoteDao.getAllQuotes().map { list -> list.map { it.toQuote() } }
    }

    fun getTodayQuote(): Flow<Quote?> {
        return quoteDao.getQuoteByDate(today()).map { it?.toQuote() }
    }

    private fun QuoteEntity.toQuote(): Quote = Quote(text, author, isBookmarked)


    // A private StateFlow to hold the latest quote, allowing read-write operations internally.
    private val _latestQuote = MutableStateFlow<Quote?>(null)

    /**
     * A public StateFlow for observing the latest motivational quote.
     * This provides a read-only stream to external consumers, ensuring reactive updates when the quote changes.
     */
    val latestQuote: StateFlow<Quote?> = _latestQuote

    /**
     * Updates the latest quote stored in the repository.
     *
     * @param quote The new motivational quote to be stored. It is a non-null [Quote] value.
     *
     * This method:
     * - Updates the `_latestQuote` value.
     * - Logs the updated quote for debugging and informational purposes using `Timber`.
     */
    fun setLatestQuote(quote: Quote) {
        _latestQuote.value = quote
        // Logs the updated quote for debugging and monitoring purposes.
        Timber.i("Latest quote set to: $quote")
    }
}