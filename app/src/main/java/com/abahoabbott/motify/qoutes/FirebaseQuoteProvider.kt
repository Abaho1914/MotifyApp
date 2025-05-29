package com.abahoabbott.motify.qoutes

import com.abahoabbott.motify.data.Quote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.LocalDate

/**
 * FirebaseQuoteProvider fetches a daily motivational quote from Firebase Firestore.
 * It uses the current date as the document ID to retrieve the quote.
 *
 * If the quote is not found or the request fails, a default quote is returned.
 *
 * @property firestore The Firestore instance used to retrieve quotes.
 */
class FirebaseQuoteProvider(
    private val firestore: FirebaseFirestore
) : QuoteProvider {


    /**
     * Retrieves the motivational quote of the day.
     *
     * @param date Optional parameter to specify the date for which to fetch the quote.
     *             Defaults to the current date. Useful for testing.
     * @return A [Quote] object containing the quote text and author.
     */
    override suspend fun getDailyQuote(date: LocalDate): Quote {   return try {
        val snapshot = firestore.collection(QUOTES_COLLECTION)
            .document(date.toString())
            .get()
            .await()

        val text = snapshot.getString("text") ?: DEFAULT_QUOTE
        val author = snapshot.getString("author") ?: DEFAULT_AUTHOR

        Quote(text, author)
    } catch (e: Exception) {
        Timber.e( "Failed to fetch quote: ${e.message}")
        Quote(DEFAULT_QUOTE, DEFAULT_AUTHOR)
    }
    }

    companion object {
        private const val DEFAULT_QUOTE = "Stay positive and keep pushing forward."
        private const val DEFAULT_AUTHOR = "Unknown"
        private const val QUOTES_COLLECTION = "quotes_of_the_day"
    }
}
