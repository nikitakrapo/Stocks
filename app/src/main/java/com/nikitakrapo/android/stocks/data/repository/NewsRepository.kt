package com.nikitakrapo.android.stocks.data.repository

import com.nikitakrapo.android.stocks.data.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.data.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.domain.model.NetworkResult

interface NewsRepository {

    fun addNews(
        marketNewsArticle: MarketNewsArticle,
        marketNewsCategory: MarketNewsCategory
    )

    fun addNews(
        marketNewsArticles: List<MarketNewsArticle>,
        marketNewsCategory: MarketNewsCategory
    )

    fun getNewsByCategory(
        marketNewsCategory: MarketNewsCategory
    ): List<MarketNewsArticle>

    fun deleteAllNews()

    fun replaceNews(
        marketNewsArticles: List<MarketNewsArticle>,
        marketNewsCategory: MarketNewsCategory
    )

    suspend fun getMarketNews(
        newsCategory: MarketNewsCategory
    ): NetworkResult<List<MarketNewsArticle>>

}