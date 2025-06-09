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
    // Uncomment this when you're ready to use Gemini API

    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )


    suspend fun generateQuote(): Result<Quote> = withContext(Dispatchers.IO) {
        try {
            if (BuildConfig.USE_FAKE_QUOTES) {
                Timber.d("Using fake quote for debug build")
                return@withContext Result.success(
                    Quote(
                        id = java.util.UUID.randomUUID().toString(),
                        text = "Fake AI quote",
                        author = "Abaho Abbott",
                        date = java.time.LocalDateTime.now().toLocalDate().toString()
                    )
                )
            }

            // Real API call

            val prompt = """
                Generate a motivational quote in the following format:
                "Quote text" - Author Name

                Requirements:
                1. The quote should be inspiring and motivational
                2. The author should be a real person
                3. Keep the quote concise (1-2 sentences)
                4. Return ONLY the quote and author in the specified format
            """.trimIndent()

            Timber.d("Generating quote with Gemini")

            val response = model.generateContent(prompt)
            val quoteText = response.text ?: throw Exception("No response from Gemini")

            val parts = quoteText.split(" - ", limit = 2)
            if (parts.size != 2) {
                throw Exception("Invalid quote format")
            }

            val text = parts[0].trim().removeSurrounding("\"")
            val author = parts[1].trim()

            Result.success(
                Quote(
                    id = java.util.UUID.randomUUID().toString(),
                    text = text,
                    author = author,
                    date = java.time.LocalDateTime.now().toLocalDate().toString()
                )
            )


            throw NotImplementedError("Gemini API integration is not yet enabled")

        } catch (e: Exception) {
            Timber.e(e, "Error generating quote")
            Result.failure(e)
        }
    }
}
