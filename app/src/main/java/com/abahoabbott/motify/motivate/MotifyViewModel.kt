package com.abahoabbott.motify.motivate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.screens.home.MotifyUiState
import com.abahoabbott.motify.screens.home.MotifyUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import java.util.UUID
import java.time.LocalDateTime


@HiltViewModel
class MotifyViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MotifyUiState>(Loading)
    val uiState: StateFlow<MotifyUiState> = _uiState.asStateFlow()


    init {
        fetchInitialQuote()
    }

    private fun fetchInitialQuote() {

        viewModelScope.launch {
            _uiState.value = Loading
            try {
                quotesRepository.getTodayQuote().collect {

                    quote->
                    if (quote == null){
                        _uiState.value = Error("No quote found")
                    } else
                    {
                        _uiState.value = Success(quote)
                    }
                }

            } catch (e: Exception){
                _uiState.value = Error(e.message.toString())
            }

        }

    }

    // Function to get the next quote
    fun getNextQuote() {

       }

    fun overrideQuoteFromNotification(quote: String) {
        _uiState.value = Success( stringToQuote(quote))
        Timber.i("Quote overridden from notification: $quote")
    }

    fun toggleFavorite(quote: Quote){
        viewModelScope.launch {
            try {
                val updatedQuote = quote.copy(
                    isSaved = !quote.isSaved
                )
                quotesRepository.updateQuote(updatedQuote)
              if (_uiState.value is Success && (uiState.value as Success).quote == quote){
                  _uiState.value = Success(updatedQuote)
              }
            } catch (e:Exception){
                Timber.e(e, "Error toggling favorite state for quote: ${quote.text}")
            }
        }
    }

    fun stringToQuote(quoteString: String): Quote {
        val parts = quoteString.split(" - ", limit = 2)
        val text = parts.getOrNull(0)?.trim() ?: ""
        val author = parts.getOrNull(1)?.trim() ?: "Unknown"
        return Quote(
            id = UUID.randomUUID().toString(),
            text = text,
            author = author,
            date = LocalDateTime.now().toLocalDate().toString()
        )
    }

}

