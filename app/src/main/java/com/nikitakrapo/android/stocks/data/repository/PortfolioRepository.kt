package com.nikitakrapo.android.stocks.data.repository

import androidx.lifecycle.LiveData
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio

interface PortfolioRepository {
    
    fun addPortfolio(stockPortfolio: StockPortfolio)

    fun getPortfolio(name: String): LiveData<StockPortfolio>

    fun getPortfolios(): LiveData<List<StockPortfolio>>

    fun deletePortfolio(name: String)

    fun deleteAllPortfolios()
}