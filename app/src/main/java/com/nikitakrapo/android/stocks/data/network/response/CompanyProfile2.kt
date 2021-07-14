package com.nikitakrapo.android.stocks.data.network.response

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
    val country: String,
    val currency: String,
    val exchange: String,
    val name: String,
    val ticker: String,
    val ipo: String,
    val marketCapitalization: Double,
    val shareOutstanding: Double,
    val logo: String,
    val phone: String,
    val weburl: String,
    val finnhubIndustry: String
)
