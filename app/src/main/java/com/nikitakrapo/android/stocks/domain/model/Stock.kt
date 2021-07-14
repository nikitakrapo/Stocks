package com.nikitakrapo.android.stocks.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikitakrapo.android.stocks.data.network.response.CompanyProfile2
import com.nikitakrapo.android.stocks.data.network.response.StockPrice

/**
 * Represents a market stock.
 * @property symbol The stocks's symbol.
 * @property stockPrice The stocks's prices (open, close, etc).
 * @property companyProfile2 The stock's company profile
 */
@Entity
data class Stock(
    @PrimaryKey
    val symbol: String,
    var stockPrice: StockPrice,
    var companyProfile2: CompanyProfile2
)