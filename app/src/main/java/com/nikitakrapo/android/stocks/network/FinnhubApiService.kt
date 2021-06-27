package com.nikitakrapo.android.stocks.network

import com.nikitakrapo.android.stocks.network.response.*
import com.nikitakrapo.android.stocks.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.network.response.enums.StockCandleResolution
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApiService {

    companion object{
        const val BASE_URL = "https://finnhub.io/api/v1/"
    }

    @GET("quote")
    fun getStockPrice(
            @Query("symbol") symbol: String
    ): Call<StockPrice>

    @GET("profile2")
    fun getCompanyProfile2(
            @Query("symbol") symbol: String
    ): Call<CompanyProfile2>

    @GET("search")
    fun getSymbolLookup(
            @Query("q") q: String
    ): Call<SymbolLookup>

    @GET("stock/candle")
    fun getStockCandle(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: StockCandleResolution,
        @Query("from") from: Long,
        @Query("to") to: Int
    ): Call<StockCandle>

    @GET("news")
    fun getMarketNews(
            @Query("category") category: MarketNewsCategory
    ): Call<List<MarketNewsArticle>>

    @GET("company-news")
    fun getCompanyNews(
            @Query("symbol") symbol: String,
            @Query("from") fromYYYY_MM_DD: String,
            @Query("to") toYYYY_MM_DD: String
    ): Call<List<MarketNewsArticle>>

}