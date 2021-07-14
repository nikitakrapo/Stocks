package com.nikitakrapo.android.stocks.data.network.response

/**
 * Represents a stock price response
 * @property o open price of the day
 * @property h high price of the day
 * @property l low price of the day
 * @property c current price
 * @property pc previous close price
 * @property t current time UNIX
 */
data class StockPrice(
    val o: Double,
    val h: Double,
    val l: Double,
    val c: Double,
    val pc: Double,
    val t: Long)