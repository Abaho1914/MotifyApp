package com.abahoabbott.motify.motivate

import com.abahoabbott.motify.data.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository class for managing motivational quotes.
 * Provides a reactive stream for observing the latest quote.
 *
 * @constructor The class is annotated with @Inject for easy dependency injection using frameworks like Dagger or Hilt.
 */
class QuotesRepository @Inject constructor() {

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