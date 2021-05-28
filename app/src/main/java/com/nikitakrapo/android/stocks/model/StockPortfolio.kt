package com.nikitakrapo.android.stocks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a stock portfolio.
 * @property name The portfolio's user chosen name.
 * @property portfolioStocks The list of portfolio's stocks' names.
 */
@Entity
data class StockPortfolio(
        @PrimaryKey
        val name: String,
        val portfolioStocks: List<String>
        )