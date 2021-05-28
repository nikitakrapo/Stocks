package com.nikitakrapo.android.stocks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a market stock.
 * @property symbol The stocks's symbol.
 * @property price The stocks's price.
 * @property currency The stocks's currency (USD, EUR,..).
 */
@Entity
data class Stock(
        @PrimaryKey
        var symbol: String,
        var price: Double,
        var currency: Currency
        )