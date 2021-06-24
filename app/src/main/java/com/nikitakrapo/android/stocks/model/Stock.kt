package com.nikitakrapo.android.stocks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikitakrapo.android.stocks.model.finnhub.CompanyProfile2

/**
 * Represents a market stock.
 * @property symbol The stocks's symbol.
 * @property price The stocks's price.
 * @property currency The stocks's currency (USD, EUR,..).
 */
@Entity
data class Stock(
        @PrimaryKey
        val symbol: String,
        var price: Double,
        var currency: Currency,
        )