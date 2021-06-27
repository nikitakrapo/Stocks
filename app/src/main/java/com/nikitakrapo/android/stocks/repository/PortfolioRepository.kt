package com.nikitakrapo.android.stocks.repository

import androidx.lifecycle.LiveData
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio
import com.nikitakrapo.android.stocks.network.FinnhubApiService
import com.nikitakrapo.android.stocks.room.PortfolioDao

class PortfolioRepository constructor(
    private val portfolioDao: PortfolioDao,
    private val finnhubApiService: FinnhubApiService
) {

    fun addPortfolio(stockPortfolio: StockPortfolio) {
        portfolioDao.addPortfolio(stockPortfolio)
    }

    fun getPortfolio(name: String): LiveData<StockPortfolio> {
        return portfolioDao.getPortfolio(name)
    }

    fun getPortfolios(): LiveData<List<StockPortfolio>> {
        return portfolioDao.getPortfolios()
    }

    fun deletePortfolio(name: String) {
        portfolioDao.deletePortfolio(name)
    }

    fun deleteAllPortfolios() {
        portfolioDao.deleteAllPortfolios()
    }
}
