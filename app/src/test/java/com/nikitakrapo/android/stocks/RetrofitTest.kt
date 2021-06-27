package com.nikitakrapo.android.stocks

import com.nikitakrapo.android.stocks.network.response.enums.StockCandleResolution
import com.nikitakrapo.android.stocks.network.FinnhubApiService
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class RetrofitTest {

    companion object{
        private const val TOO_MANY_REQUESTS = 429
    }

    @Inject
    private lateinit var remoteApi: FinnhubApiService

    @Test
    fun tooManyRequests(){
        val call = remoteApi.getStockPrice("AAPL")
        val response = call.execute()
        val isSuccessful = response.isSuccessful
    }

    @Test
    fun wrongTime(){
        val call = remoteApi.getStockCandle(
                "AAPL",
                StockCandleResolution.DAY,
                0,
                0)

        val response = call.execute()
        if (response.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals("no_data", response.body()?.s)
    }

    @Test
    fun wrongSymbol(){
        val call = remoteApi.getStockCandle(
                "SASDOQJASDPASmnASDJi0o",
                StockCandleResolution.DAY,
                1621911069,
                1622101869)

        val response = call.execute()
        if (response.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals("no_data", response.body()?.s)
    }

    @Test
    fun wrongSymbolPrice(){
        val call = remoteApi.getStockPrice("ASDKASOD")
        var response = call.execute()
        if (response.code() == TOO_MANY_REQUESTS){
            throw Exception("Too many requests")
        }
        assertEquals(0.0, response.body()?.c)
        assertEquals(0.0, response.body()?.h)
        assertEquals(0.0, response.body()?.l)
        assertEquals(0.0, response.body()?.o)
        assertEquals(0.0, response.body()?.pc)
        assertEquals(0L, response.body()?.t)
    }
}