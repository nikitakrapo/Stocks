package com.nikitakrapo.android.stocks.repository

import android.content.Context
import com.nikitakrapo.android.stocks.model.Result
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.StockPrice
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import com.nikitakrapo.android.stocks.room.StockMarketDatabase
import java.io.IOException

class StockRepository private constructor(context: Context) {

    companion object {
        private lateinit var stockMarketDatabase: StockMarketDatabase
        private lateinit var finnhubApiService: FinnhubApiService

        private var stockRepository: StockRepository? = null

        fun getInstance(context: Context): StockRepository {
            if (stockRepository == null) {
                stockRepository = StockRepository(context)
                stockMarketDatabase = StockMarketDatabase.getClient(context)
                finnhubApiService = FinnhubApiService.finnhubApiService
            }
            return stockRepository!!
        }
    }

    suspend fun getStockPriceFromApi(symbol: String) = safeApiCall(
            call = { stockPriceFromApi(symbol) },
            errorMessage = "Error occurred"
    )

    private suspend fun stockPriceFromApi(symbol: String): Result<StockPrice> {
        val response = finnhubApiService.getStockPrice(symbol).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()?.t == 0L) { // Finnhub retrieves zeros in case of a wrong symbol
                return Result.Error(IllegalArgumentException("Wrong symbol"))
            }
            return Result.Success(response.body()!!)
        }
        return Result.Error(Exception("Unsuccessful response"))
    }

    suspend fun getMarketNews(newsCategory: MarketNewsCategory) = safeApiCall(
            call = { marketNews(newsCategory) },
            errorMessage = "Unknown exception"
    )

    private suspend fun marketNews(newsCategory: MarketNewsCategory): Result<List<MarketNewsArticle>> {
        val response = finnhubApiService.getMarketNews(newsCategory).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()!!.isEmpty()) {
                return Result.Error(Exception("No news"))
            }
            return Result.Success(response.body()!!)
        }
        return Result.Error(Exception("Unsuccessful response\""))
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> = try {
        call.invoke()
    } catch (e: Exception) {
        Result.Error(IOException(errorMessage, e))
    }
}
