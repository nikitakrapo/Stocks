package com.nikitakrapo.android.stocks.model

/**
 * Represents a market stock.
 * @property symbol The stocks's symbol.
 * @property price The stocks's price.
 * @property currency The stocks's currency (USD, EUR,..).
 */
data class Stock(
        var symbol: String,
        var price: Double,
        var currency: Currency
        )