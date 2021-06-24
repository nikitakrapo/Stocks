package com.nikitakrapo.android.stocks.repository

import android.content.Context
import com.nikitakrapo.android.stocks.model.HttpStatusCode
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.model.finnhub.StockPrice
import com.nikitakrapo.android.stocks.model.finnhub.SymbolLookup
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
            call = { stockPriceFromApi(symbol) }
    )

    private fun stockPriceFromApi(symbol: String): NetworkResult<StockPrice> {
        val response = finnhubApiService.getStockPrice(symbol).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()?.t == 0L) { // Finnhub retrieves zeros in case of a wrong symbol
                return NetworkResult.Error(IllegalArgumentException("Wrong symbol"))
            }
            return NetworkResult.Success(response.body()!!)
        }
        return NetworkResult.Error(
            Exception("Unsuccessful Response"),
            HttpStatusCode.fromInt(response.code()) ?: HttpStatusCode.Unknown
        )
    }

    suspend fun getSymbolLookup(queue: String) = safeApiCall(
        call = { symbolLookup(queue) }
    )

    private fun symbolLookup(queue: String): NetworkResult<SymbolLookup> {
        val response = finnhubApiService.getSymbolLookup(queue).execute()
        if (response.isSuccessful && response.body() != null) {
            return NetworkResult.Success(response.body()!!)
        }
        return NetworkResult.Error(
            Exception("Unsuccessful Response"),
            HttpStatusCode.fromInt(response.code()) ?: HttpStatusCode.Unknown
        )
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> NetworkResult<T>): NetworkResult<T> = try {
        call.invoke()
    } catch (e: Exception) {
        NetworkResult.Error(IOException(e))
    }
}
