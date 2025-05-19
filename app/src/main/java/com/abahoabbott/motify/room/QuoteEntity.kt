package com.abahoabbott.motify.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("QuotesTable")
data class QuoteEntity(
    @PrimaryKey
    val date: String, //Format ,YYYY-MM-DD
    val text: String,
    val author: String,
    val isBookmarked: Boolean = false
)

