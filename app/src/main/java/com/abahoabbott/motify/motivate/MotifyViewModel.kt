package com.abahoabbott.motify.motivate

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MotifyViewModel @Inject constructor(): ViewModel() {
    private val motivationQuotes = listOf(
        "The biggest temptation is to settle for too little.",
        "Your only limit is your mind.",
        "Success is not final, failure is not fatal: It is the courage to continue that counts.",
        "It always seems impossible until it's done.",
        "The future belongs to those who believe in the beauty of their dreams."
    )

    // Current quote state
    private var _currentQuoteIndex = mutableIntStateOf(0)
    val currentQuote: String
        get() = motivationQuotes[_currentQuoteIndex.intValue]

    // Function to get the next quote
    fun getNextQuote() {
        _currentQuoteIndex.intValue = (_currentQuoteIndex.intValue + 1) % motivationQuotes.size
    }
}

