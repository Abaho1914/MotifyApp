package com.abahoabbott.motify.screens.home

import com.abahoabbott.motify.data.Quote

sealed class MotifyUiState() {
    data object Loading : MotifyUiState()
    data class Error(val message: String) : MotifyUiState()
    data class Success(val quote: Quote) : MotifyUiState()
}