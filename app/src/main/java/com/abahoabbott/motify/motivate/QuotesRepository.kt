package com.abahoabbott.motify.motivate

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

class QuotesRepository @Inject constructor(){

    private val _latestQuote = MutableStateFlow<String?>(null)
    val latestQuote: StateFlow<String?> = _latestQuote

    fun setLatestQuote(quote: String) {
        _latestQuote.value = quote
        Timber.i("Latest quote set to: $quote")
    }

}
