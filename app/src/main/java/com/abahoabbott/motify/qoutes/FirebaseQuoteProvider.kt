package com.abahoabbott.motify.qoutes

import com.abahoabbott.motify.data.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.LocalDate

/**
 * FirebaseQuoteProvider fetches a daily motivational quote from Firebase Firestore.
 * It uses the current date as the document ID to retrieve the quote.
 * Falls back to the latest quote if the current date is missing.
 */
class FirebaseQuoteProvider(
    private val firestore: FirebaseFirestore
) : QuoteProvider {

    /**
     * Retrieves the motivational quote of the day.
     * If not found, returns the most recent fallback or a default quote.
     *
     * @param date The date to fetch the quote for (defaults to today).
     * @return A [Quote] object containing the quote text and author.
     */
    override suspend fun getDailyQuote(date: LocalDate): Quote {
        return try {
            val docRef = firestore.collection(QUOTES_COLLECTION)
                .document(date.toString())
                .get()
                .await()

            if (docRef.exists()) {
                val text = docRef.getString("text") ?: DEFAULT_QUOTE
                val author = docRef.getString("author") ?: DEFAULT_AUTHOR
                Quote(text = text, author=author)
            } else {
                // Fallback to latest available quote
                val fallback = firestore.collection(QUOTES_COLLECTION)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()

                val fallbackText = fallback?.getString("text") ?: DEFAULT_QUOTE
                val fallbackAuthor = fallback?.getString("author") ?: DEFAULT_AUTHOR
                Quote(text = fallbackText, author = fallbackAuthor)
            }
        } catch (e: Exception) {
            Timber.e("Failed to fetch quote: ${e.message}")
            Quote(author = DEFAULT_AUTHOR, text = DEFAULT_QUOTE)
        }
    }

    companion object {
        private const val DEFAULT_QUOTE = "Stay positive and keep pushing forward."
        private const val DEFAULT_AUTHOR = "Unknown"
        private const val QUOTES_COLLECTION = "quotes_of_the_day"
    }
}
