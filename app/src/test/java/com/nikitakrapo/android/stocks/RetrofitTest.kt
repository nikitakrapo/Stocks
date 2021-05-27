package com.nikitakrapo.android.stocks

import android.util.Log
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import org.junit.Test
import retrofit2.Call
import retrofit2.http.Url

class RetrofitTest {

    companion object{
        private const val TOO_MANY_REQUESTS = 429
    }

    @Test
    fun properEnumConvert() {

        val remoteApi = FinnhubApiService.finnhubApiService

        val call = remoteApi.getStockCandle(
                "AAPL",
                FinnhubApiService.StockCandleResolution.DAY,
                1000000,
                1500000)

        val request = call.request()
        val resParameter = request.url().queryParameterValues("resolution").firstOrNull()
        assertEquals("D", resParameter)
    }

    @Test
    fun simpleRequest(){
        val remoteApi = FinnhubApiService.finnhubApiService

        val call = remoteApi.getStockCandle(
                "AAPL",
                FinnhubApiService.StockCandleResolution.DAY,
                1621911069,
                1622101869)

        val result = call.execute()
        if (result.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals("ok", result.body()?.s)
    }

    @Test
    fun wrongTime(){
        val remoteApi = FinnhubApiService.finnhubApiService

        val call = remoteApi.getStockCandle(
                "AAPL",
                FinnhubApiService.StockCandleResolution.DAY,
                0,
                0)

        val result = call.execute()
        if (result.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals("no_data", result.body()?.s)
    }

    @Test
    fun wrongSymbol(){
        val remoteApi = FinnhubApiService.finnhubApiService

        val call = remoteApi.getStockCandle(
                "SASDOQJASDPASmnASDJi0o",
                FinnhubApiService.StockCandleResolution.DAY,
                1621911069,
                1622101869)

        val result = call.execute()
        if (result.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals("no_data", result.body()?.s)
    }
}