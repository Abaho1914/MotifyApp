package com.abahoabbott.motify.data

import com.abahoabbott.motify.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateQuote(isTesting: Boolean = BuildConfig.DEBUG): Result<Quote> =
        withContext(Dispatchers.IO) {
            return@withContext if (isTesting) {
                generateTestQuote()
            } else {
                generateRealQuote()
            }
        }

    private fun generateTestQuote(): Result<Quote> {
        Timber.d("Using test quote for debug/testing mode")
        return Result.success(
            Quote(
                id = java.util.UUID.randomUUID().toString(),
                text = "No matter what happens in life, be good to people",
                author = "Taylor Swift",
                date = java.time.LocalDateTime.now().toLocalDate().toString()
            )
        )
    }

    private suspend fun generateRealQuote(): Result<Quote> {
        return try {
            Timber.d("Starting quote generation with Gemini API")

            val response = model.generateContent(PROMPT)
            val quoteText = response.text

            if (quoteText.isNullOrBlank()) {
                val error = GeminiException.EmptyResponse("Gemini returned empty or null response")
                Timber.w(error, "Empty response from Gemini API")
                return Result.failure(error)
            }

            Timber.d("Raw Gemini response: $quoteText")
            parseQuoteResponse(quoteText)

        } catch (e: Exception) {
            handleGeminiError(e)
        }
    }

    private fun parseQuoteResponse(quoteText: String): Result<Quote> {
        return try {
            val cleanedText = quoteText.trim().replace("\n", " ")
            Timber.d("Parsing quote text: $cleanedText")

            // More flexible parsing to handle various formats
            val parts = when {
                cleanedText.contains(" - ") -> cleanedText.split(" - ", limit = 2)
                cleanedText.contains(" — ") -> cleanedText.split(" — ", limit = 2)  // em dash
                cleanedText.contains(" – ") -> cleanedText.split(" – ", limit = 2)   // en dash
                else -> {
                    val error =
                        GeminiException.InvalidFormat("Quote format not recognized: $cleanedText")
                    Timber.w(error, "Failed to parse quote format")
                    return Result.failure(error)
                }
            }

            if (parts.size != 2) {
                val error =
                    GeminiException.InvalidFormat("Expected format: \"Quote text\" - Author Name. Got: $cleanedText")
                Timber.w(error, "Quote parsing failed - incorrect number of parts: ${parts.size}")
                return Result.failure(error)
            }

            val text = parts[0].trim()
                .removeSurrounding("\"")
                .removeSurrounding(
                    """)  // Handle smart quotes
                .removeSurrounding("""
                )
                .trim()

            val author = parts[1].trim()

            // Validate parsed content
            if (text.isBlank()) {
                val error = GeminiException.InvalidContent("Quote text is empty after parsing")
                Timber.w(error, "Empty quote text after parsing")
                return Result.failure(error)
            }

            if (author.isBlank()) {
                val error = GeminiException.InvalidContent("Author name is empty after parsing")
                Timber.w(error, "Empty author name after parsing")
                return Result.failure(error)
            }

            val quote = Quote(
                id = java.util.UUID.randomUUID().toString(),
                text = text,
                author = author,
                date = java.time.LocalDateTime.now().toLocalDate().toString()
            )

            Timber.i("Successfully generated quote: \"${quote.text}\" - ${quote.author}")
            Result.success(quote)

        } catch (e: Exception) {
            val error = GeminiException.ParseError("Failed to parse quote response", e)
            Timber.e(error, "Quote parsing failed with exception")
            Result.failure(error)
        }
    }

    private fun handleGeminiError(exception: Exception): Result<Quote> {
        val error = when (exception) {
            is java.net.UnknownHostException -> {
                Timber.w(exception, "Network connectivity issue")
                GeminiException.NetworkError("No internet connection available", exception)
            }

            is java.net.SocketTimeoutException -> {
                Timber.w(exception, "Request timeout")
                GeminiException.TimeoutError("Request timed out", exception)
            }

            is javax.net.ssl.SSLException -> {
                Timber.w(exception, "SSL/TLS error")
                GeminiException.NetworkError("SSL connection failed", exception)
            }

            is java.io.IOException -> {
                Timber.w(exception, "IO error during API call")
                GeminiException.NetworkError("Network IO error", exception)
            }

            is SecurityException -> {
                Timber.e(exception, "Security error - possibly invalid API key")
                GeminiException.AuthenticationError("Authentication failed", exception)
            }

            else -> {
                Timber.e(exception, "Unexpected error during quote generation")
                GeminiException.UnknownError("Unexpected error occurred", exception)
            }
        }

        return Result.failure(error)
    }

    companion object {
        private val PROMPT = """
       Please generate a **motivational quote** in the **exact format** below:

       "Quote text" - Author Name

       Rules:
       1. The quote must be **genuinely motivational**, suitable for daily inspiration.
       2. The **author must be a real and notable person** (e.g., scientists, leaders, artists,writers).
       3. The quote should be **concise** (1–2 sentences maximum).
       4. Return the output in the **exact format above**, with NO extra text or explanation.

    """.trimIndent()
    }



}

// Custom exception classes for better error handling
sealed class GeminiException(message: String, cause: Throwable? = null) :
    Exception(message, cause) {
    class EmptyResponse(message: String) : GeminiException(message)
    class InvalidFormat(message: String) : GeminiException(message)
    class InvalidContent(message: String) : GeminiException(message)
    class ParseError(message: String, cause: Throwable) : GeminiException(message, cause)
    class NetworkError(message: String, cause: Throwable) : GeminiException(message, cause)
    class TimeoutError(message: String, cause: Throwable) : GeminiException(message, cause)
    class AuthenticationError(message: String, cause: Throwable) : GeminiException(message, cause)
    class UnknownError(message: String, cause: Throwable) : GeminiException(message, cause)
}