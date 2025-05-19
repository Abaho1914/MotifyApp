package com.abahoabbott.motify.motivate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahoabbott.motify.data.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MotifyViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository
) : ViewModel() {
    private val motivationQuotes = listOf(
        Quote("The biggest temptation is to settle for too little.", "Unknown"),
        Quote("Your only limit is your mind.", "Unknown"),
        Quote(
            "Success is not final, failure is not fatal: It is the courage to continue that counts.",
            "Unknown"
        ),
        Quote("It always seems impossible until it's done.", "Unknown"),
        Quote(
            "The future belongs to those who believe in the beauty of their dreams.",
            "Eleanor Roosevelt"
        ) // Known author
    )

    private val _currentQuote = MutableStateFlow(Quote("", ""))
    val currentQuote: StateFlow<Quote> = _currentQuote

    init {
        // Show latest from worker, or fallback to list

        viewModelScope.launch {
            quotesRepository.latestQuote.collect { quote ->
                if (quote != null) {
                    _currentQuote.value = quote
                } else {
                    _currentQuote.value = motivationQuotes[0]
                }
            }
        }

    }

    // Function to get the next quote
    fun getNextQuote() {
        val currentIndex = motivationQuotes.indexOf(_currentQuote.value)
        val nextIndex = (currentIndex + 1) % motivationQuotes.size
        _currentQuote.value = motivationQuotes[nextIndex]
        Timber.i("Next quote: ${_currentQuote.value}")
    }

    fun overrideQuoteFromNotification(quote: String) {
        _currentQuote.value = stringToQuote(quote)
        Timber.i("Quote overridden from notification: $quote")
    }

    fun stringToQuote(quoteString: String): Quote {
        val parts = quoteString.split(" - ", limit = 2)
        val text = parts.getOrNull(0)?.trim() ?: ""
        val author = parts.getOrNull(1)?.trim() ?: "Unknown"
        return Quote(text, author)
    }

}

