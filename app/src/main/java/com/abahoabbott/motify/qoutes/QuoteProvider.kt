package com.abahoabbott.motify.qoutes

import com.abahoabbott.motify.data.Quote
import java.time.LocalDate


/**
 * Defines a contract for providing motivational quotes.
 * Implementations of this interface are responsible for sourcing and returning quotes.
 */
interface QuoteProvider {

   suspend  fun getDailyQuote(date: LocalDate): Quote
  // suspend fun generateNewQuote(): Result<Quote>
}

