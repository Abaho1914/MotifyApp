package com.abahoabbott.motify.data

import java.util.UUID

data class Quote(
    val id: String =UUID.randomUUID().toString(),
    val text: String,
    val author: String,
    val date: String="",
    val isSaved: Boolean = false
) {
    override fun toString(): String {
        return "$text - $author"
    }
}