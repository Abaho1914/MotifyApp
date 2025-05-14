package com.abahoabbott.motify.motivate

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MotifyViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository
): ViewModel() {
    private val motivationQuotes = listOf(
        "The biggest temptation is to settle for too little.",
        "Your only limit is your mind.",
        "Success is not final, failure is not fatal: It is the courage to continue that counts.",
        "It always seems impossible until it's done.",
        "The future belongs to those who believe in the beauty of their dreams."

    )

    private val _currentQuote = MutableStateFlow("")
    val currentQuote: StateFlow<String> = _currentQuote

    init {
        // Show latest from worker, or fallback to list

        viewModelScope.launch {
            quotesRepository.latestQuote.collect {

                quote->
                if (quote!= null){
                    _currentQuote.value = quote
                }  else{
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
}

