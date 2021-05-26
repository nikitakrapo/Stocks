package com.nikitakrapo.android.stocks.retrofit

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApiService {

    companion object{
        private const val BASE_URL = "https://finnhub.io/api/v1/"
        val finnhubApiService: FinnhubApiService
            get() =
                RetrofitClient
                    .getClient(BASE_URL)
                    .create(FinnhubApiService::class.java)
    }

    @GET("quote")
    fun getStockPrice(@Query("symbol") symbol: String): Deferred<StockPrice>

    @GET("profile2/")
    fun getCompanyProfile2(@Query("symbol") symbol: String): Deferred<CompanyProfile2>

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
        val o: Double,
        val h: Double,
        val l: Double,
        val c: Double,
        val pc: Double,
        val t: Long)

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

}