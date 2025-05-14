package com.abahoabbott.motify.qoutes

interface QuoteProvider {

    fun getDailyQuote(): String
}

class DefaultMotifyQuoteProvider : QuoteProvider{

    private val quotes = listOf<String>(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
        "Strive not to be a success, but rather to be of value. - Albert Einstein",
        "The mind is everything. What you think you become. - Buddha",
        "Everything you've ever wanted is on the other side of fear. - George Addair",
        "Start where you are. Use what you have. Do what you can. - Arthur Ashe",
        "The most difficult thing is the decision to act, the rest is merely tenacity. - Amelia Earhart",
        "Your time is limited, don't waste it living someone else's life. - Steve Jobs",
        "Build your own dreams, or someone else will hire you to build theirs. - Farrah Gray",
        "The only limit to our realization of tomorrow will be our doubts of today. - Franklin D. Roosevelt",
        "Do what you can, with what you have, where you are. - Theodore Roosevelt",
        "The best way to predict the future is to create it. - Peter Drucker",
        "The only person you are destined to become is the person you decide to be. - Ralph Waldo Emerson",
        "Don't be afraid to give up the good to go for the great. - John D. Rockefeller",
        "If you are working on something that you really care about, you don’t have to be pushed. The vision pulls you. - Steve Jobs",
        "The only way to achieve the impossible is to believe it is possible. - Charles Kingsleigh (from Alice in Wonderland)",
        "The man who has confidence in himself gains the confidence of others. - Hasidic Proverb",
    )
    override fun getDailyQuote(): String {
        return quotes.random()
    }
}
