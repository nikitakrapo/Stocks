package com.nikitakrapo.android.stocks.retrofit

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApiService {

    enum class StockCandleResolution(val value: String){
        @SerializedName("1")
        ONE_MINUTE("1"),
        @SerializedName("5")
        FIVE_MINUTES("5"),
        @SerializedName("15")
        FIFTEEN_MINUTES("15"),
        @SerializedName("30")
        THIRTY_MINUTES("30"),
        @SerializedName("60")
        SIXTY_MINUTES("60"),
        @SerializedName("D")
        DAY("D"),
        @SerializedName("W")
        WEEK("W"),
        @SerializedName("M")
        MONTH("M")
    }

    enum class MarketNewsCategory(val value: String){
        @SerializedName("general")
        GENERAL("general"),
        @SerializedName("forex")
        FOREX("forex"),
        @SerializedName("crypto")
        CRYPTO("crypto"),
        @SerializedName("merger")
        MERGER("merger")
    }

    companion object{
        private const val BASE_URL = "https://finnhub.io/api/v1/"
        val finnhubApiService: FinnhubApiService
            get() =
                RetrofitClient
                    .getClient(BASE_URL)
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

    /**
     * Represents a stock price response
     * @property o open price of the day
     * @property h high price of the day
     * @property l low price of the day
     * @property c current price
     * @property pc previous close price
     * @property t current time UNIX
     */
    data class StockPrice(
        val o: Double?,
        val h: Double?,
        val l: Double?,
        val c: Double?,
        val pc: Double?,
        val t: Long?)

    /**
     * Represents a stock profile response
     * @property country country of company's headquarter
     * @property currency currency used in company filings
     * @property exchange listed exchange
     * @property name company name
     * @property ticker company's ticker symbol as used on the listed exchange
     * @property ipo ipo date year-month-day
     * @property marketCapitalization market capitalization
     * @property shareOutstanding number of outstanding shares
     * @property logo logo image url
     * @property phone company phone number
     * @property weburl company website
     * @property finnhubIndustry finnhub industry classification
     */
    data class CompanyProfile2(
        val country: String?,
        val currency: String?,
        val exchange: String?,
        val name: String?,
        val ticker: String?,
        val ipo: String?,
        val marketCapitalization: Double?,
        val shareOutstanding: Double?,
        val logo: String?,
        val phone: String?,
        val weburl: String?,
        val finnhubIndustry: String?)

    /**
     * Represents a symbol lookup response
     * @property count number of results
     * @property result array of search results
     */
    data class SymbolLookup(val count: Int,
                            val result: List<SingleLookupResponse>){
        /**
         * Represents an element in symbol lookup response
         * DO NOT USE ANYWHERE FROM symbolLookup!!
         * @property description symbol description
         * @property displaySymbol display symbol name
         * @property symbol unique symbol used to identify this symbol used in /stock/candle endpoint
         * @property type security type
         */
        data class SingleLookupResponse(
                val description: String?,
                val displaySymbol: String?,
                val symbol: String?,
                val type: String?)
    }

    /**
     * Represents a stock prices candle
     * @property s status of the response. this field can either be ok or no_data
     * @property o list of open prices for returned candles
     * @property h list of open prices for returned candles
     * @property l list of low prices for returned candles
     * @property c list of close prices for returned candles
     * @property v list of volume data for returned candles
     * @property t list of timestamp for returned candles
     */
    data class StockCandle(val s: String,
                           val o: List<Double>?,
                           val h: List<Double>?,
                           val l: List<Double>?,
                           val c: List<Double>?,
                           val v: List<Double>?,
                           val t: List<Double>?)

    /**
     * Represents a market news article
     * @property category News category.
     * @property datetime Published time in UNIX timestamp.
     * @property headline News headline.
     * @property id News ID. This value can be used for minId params to get the latest news only.
     * @property image Thumbnail image URL.
     * @property related Related stocks and companies mentioned in the article.
     * @property source News source.
     * @property summary News summary.
     * @property url URL of the original article.
     */
    data class MarketNewsArticle(val category: String?,
                                 val datetime: Long?,
                                 val headline: String?,
                                 val id: Int?,
                                 val image: String?,
                                 val related: String?,
                                 val source: String?,
                                 val summary: String?,
                                 val url: String?)

}