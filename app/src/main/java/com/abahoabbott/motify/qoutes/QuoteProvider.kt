package com.abahoabbott.motify.qoutes

import com.abahoabbott.motify.data.Quote


/**
 * Defines a contract for providing motivational quotes.
 * Implementations of this interface are responsible for sourcing and returning quotes.
 */
interface QuoteProvider {

    fun getDailyQuote(): Quote
}

class DefaultMotifyQuoteProvider : QuoteProvider {

    val quotes = listOf(
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote(
            "The future belongs to those who believe in the beauty of their dreams.",
            "Eleanor Roosevelt"
        ),
        Quote("Strive not to be a success, but rather to be of value.", "Albert Einstein"),
        Quote("The mind is everything. What you think you become.", "Buddha"),
        Quote("Everything you've ever wanted is on the other side of fear.", "George Addair"),
        Quote("Start where you are. Use what you have. Do what you can.", "Arthur Ashe"),
        Quote(
            "The most difficult thing is the decision to act, the rest is merely tenacity.",
            "Amelia Earhart"
        ),
        Quote("Your time is limited, don't waste it living someone else's life.", "Steve Jobs"),
        Quote(
            "Build your own dreams, or someone else will hire you to build theirs.",
            "Farrah Gray"
        ),
        Quote(
            "The only limit to our realization of tomorrow will be our doubts of today.",
            "Franklin D. Roosevelt"
        ),
        Quote("Do what you can, with what you have, where you are.", "Theodore Roosevelt"),
        Quote("The best way to predict the future is to create it.", "Peter Drucker"),
        Quote(
            "The only person you are destined to become is the person you decide to be.",
            "Ralph Waldo Emerson"
        ),
        Quote("Don't be afraid to give up the good to go for the great.", "John D. Rockefeller"),
        Quote(
            "If you are working on something that you really care about, you don’t have to be pushed. The vision pulls you.",
            "Steve Jobs"
        ),
        Quote(
            "The only way to achieve the impossible is to believe it is possible.",
            "Charles Kingsleigh (from Alice in Wonderland)"
        ),
        Quote(
            "The man who has confidence in himself gains the confidence of others.",
            "Hasidic Proverb"
        )
    )

    override fun getDailyQuote(): Quote {
        return quotes.random()
    }
}
