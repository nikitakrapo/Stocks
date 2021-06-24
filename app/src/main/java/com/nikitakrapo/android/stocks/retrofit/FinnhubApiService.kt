package com.nikitakrapo.android.stocks.retrofit

import com.nikitakrapo.android.stocks.model.finnhub.*
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.model.finnhub.enums.StockCandleResolution
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApiService {

    companion object{
        private const val BASE_URL = "https://finnhub.io/api/v1/"
        val finnhubApiService: FinnhubApiService
            get() =
                RetrofitClient
                    .getFinnhubClient(BASE_URL)
                    .create(FinnhubApiService::class.java)
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