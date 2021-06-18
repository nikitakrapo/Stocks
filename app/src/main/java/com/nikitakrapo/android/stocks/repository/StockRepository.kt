package com.nikitakrapo.android.stocks.repository

import android.content.Context
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.model.finnhub.StockPrice
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

    private suspend fun stockPriceFromApi(symbol: String): NetworkResult<StockPrice> {
        val response = finnhubApiService.getStockPrice(symbol).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()?.t == 0L) { // Finnhub retrieves zeros in case of a wrong symbol
                return NetworkResult.Error(IllegalArgumentException("Wrong symbol"))
            }
            return NetworkResult.Success(response.body()!!)
        }
        return NetworkResult.Error(Exception("Unsuccessful response"))
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> NetworkResult<T>, errorMessage: String): NetworkResult<T> = try {
        call.invoke()
    } catch (e: Exception) {
        NetworkResult.Error(IOException(errorMessage, e))
    }
}
