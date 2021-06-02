package com.nikitakrapo.android.stocks.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.StockPrice
import com.nikitakrapo.android.stocks.room.StockMarketDatabase
import retrofit2.await
import java.io.IOException

class PortfolioRepository private constructor(context: Context) {

    companion object {
        private lateinit var stockMarketDatabase: StockMarketDatabase
        private lateinit var finnhubApiService: FinnhubApiService

        private var repository: PortfolioRepository? = null

        fun getInstance(context: Context): PortfolioRepository{
            if (repository == null){
                repository = PortfolioRepository(context)
                stockMarketDatabase = StockMarketDatabase.getClient(context)
                finnhubApiService = FinnhubApiService.finnhubApiService
            }
            return repository!!
        }
    }

    fun addPortfolio(stockPortfolio: StockPortfolio){
        stockMarketDatabase.portfolioDao().addPortfolio(stockPortfolio)
    }

    fun getPortfolio(name: String): LiveData<StockPortfolio> {
        return stockMarketDatabase.portfolioDao().getPortfolio(name)
    }

    fun deletePortfolio(name: String){
        stockMarketDatabase.portfolioDao().deletePortfolio(name)
    }

    fun deleteAllPortfolios(){
        stockMarketDatabase.portfolioDao().deleteAllPortfolios()
    }
}