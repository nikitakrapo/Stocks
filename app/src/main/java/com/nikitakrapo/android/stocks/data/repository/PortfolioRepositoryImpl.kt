package com.nikitakrapo.android.stocks.data.repository

import androidx.lifecycle.LiveData
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio
import com.nikitakrapo.android.stocks.data.network.FinnhubApiService
import com.nikitakrapo.android.stocks.data.cache.room.PortfolioDao

class PortfolioRepositoryImpl constructor(
    private val portfolioDao: PortfolioDao,
    private val finnhubApiService: FinnhubApiService
) : PortfolioRepository {

    override fun addPortfolio(stockPortfolio: StockPortfolio) {
        portfolioDao.addPortfolio(stockPortfolio)
    }

    override fun getPortfolio(name: String): LiveData<StockPortfolio> {
        return portfolioDao.getPortfolio(name)
    }

    override fun getPortfolios(): LiveData<List<StockPortfolio>> {
        return portfolioDao.getPortfolios()
    }

    override fun deletePortfolio(name: String) {
        portfolioDao.deletePortfolio(name)
    }

    override fun deleteAllPortfolios() {
        portfolioDao.deleteAllPortfolios()
    }
}
