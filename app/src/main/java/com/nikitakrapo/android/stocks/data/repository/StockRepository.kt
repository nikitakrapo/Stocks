package com.nikitakrapo.android.stocks.data.repository

import com.nikitakrapo.android.stocks.data.network.response.StockPrice
import com.nikitakrapo.android.stocks.data.network.response.SymbolLookup
import com.nikitakrapo.android.stocks.domain.model.NetworkResult

interface StockRepository {

    suspend fun getStockPriceFromApi(
        symbol: String
    ): NetworkResult<StockPrice>

    suspend fun getSymbolLookup(
        queue: String
    ): NetworkResult<SymbolLookup>

}