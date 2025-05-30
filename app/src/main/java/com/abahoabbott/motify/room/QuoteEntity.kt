package com.abahoabbott.motify.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("QuotesTable")
data class QuoteEntity(
    val id: String = UUID.randomUUID().toString(),
    @PrimaryKey
    val date: String,
    val text: String,
    val author: String,
    val isBookmarked: Boolean = false
)

