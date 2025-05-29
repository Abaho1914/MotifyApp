package com.abahoabbott.motify.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("QuotesTable")
data class QuoteEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val date: String, // Format: "yyyy-MM-dd HH:mm:ss"
    val text: String,
    val author: String,
    val isBookmarked: Boolean = false
)

