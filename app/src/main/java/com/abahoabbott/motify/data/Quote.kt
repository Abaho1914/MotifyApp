package com.abahoabbott.motify.data

data class Quote(
    val text: String,
    val author: String,
    val date: String="",
    val isSaved: Boolean = false
) {
    override fun toString(): String {
        return "$text - $author"
    }
}