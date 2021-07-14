package com.nikitakrapo.android.stocks.data.repository

import com.nikitakrapo.android.stocks.domain.model.HttpStatusCode
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.data.network.FinnhubApiService
import com.nikitakrapo.android.stocks.data.network.response.StockPrice
import com.nikitakrapo.android.stocks.data.network.response.SymbolLookup
import com.nikitakrapo.android.stocks.data.cache.room.StockDao
import java.io.IOException

class StockRepositoryImpl constructor(
    private val stockDao: StockDao,
    private val finnhubApiService: FinnhubApiService
) : StockRepository {

    override suspend fun getStockPriceFromApi(symbol: String) = safeApiCall(
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

    override suspend fun getSymbolLookup(queue: String) = safeApiCall(
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
