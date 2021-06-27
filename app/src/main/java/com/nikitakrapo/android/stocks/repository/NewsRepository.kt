package com.nikitakrapo.android.stocks.repository

import com.nikitakrapo.android.stocks.domain.model.HttpStatusCode
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.network.FinnhubApiService
import com.nikitakrapo.android.stocks.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.network.response.NewsArticleCategory
import com.nikitakrapo.android.stocks.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.room.NewsCategoriesDao
import com.nikitakrapo.android.stocks.room.NewsDao
import java.io.IOException

class NewsRepository constructor(
    private val newsDao: NewsDao,
    private val newsCategoriesDao: NewsCategoriesDao,
    private val finnhubApiService: FinnhubApiService
) {

    fun addNews(marketNewsArticle: MarketNewsArticle, marketNewsCategory: MarketNewsCategory) {
        newsDao.addNews(marketNewsArticle)
        newsCategoriesDao.addNewsArticleWithCategory(
            NewsArticleCategory(marketNewsArticle.id, marketNewsCategory.value)
        )
    }

    fun addNews(
        marketNewsArticles: List<MarketNewsArticle>,
        marketNewsCategory: MarketNewsCategory
    ) {
        newsDao.addNews(marketNewsArticles)
        newsCategoriesDao
            .addNewsArticleWithCategory(
                marketNewsArticles.map {
                    NewsArticleCategory(it.id, marketNewsCategory.value)
                }.toList()
            )
    }

    fun getNewsByCategory(marketNewsCategory: MarketNewsCategory): List<MarketNewsArticle> {
        val ids = newsCategoriesDao.getIdsByCategory(marketNewsCategory)
        return newsDao.getNewsByIdsOrdered(ids)
    }

    fun deleteAllNews() {
        newsDao.deleteAllNews()
        newsCategoriesDao.deleteAllNewsArticleWithCategories()
    }

    fun replaceNews(
        marketNewsArticles: List<MarketNewsArticle>,
        marketNewsCategory: MarketNewsCategory
    ) {
        val ids = newsCategoriesDao.getIdsByCategory(marketNewsCategory)
        newsDao.deleteNewsByIds(ids)
        newsCategoriesDao.deleteByCategory(marketNewsCategory)
        addNews(marketNewsArticles, marketNewsCategory)
    }

    suspend fun getMarketNews(newsCategory: MarketNewsCategory) = safeApiCall(
        call = { marketNews(newsCategory) }
    )

    private fun marketNews(marketNewsCategory: MarketNewsCategory): NetworkResult<List<MarketNewsArticle>> {
        val response = finnhubApiService.getMarketNews(marketNewsCategory).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()!!.isEmpty())
                return NetworkResult.Error(Exception("No news"))
            replaceNews(response.body()!!, marketNewsCategory)
            return NetworkResult.Success(response.body()!!)
        }
        return NetworkResult.Error(
            Exception("Unsuccessful Response"),
            HttpStatusCode.fromInt(response.code()) ?: HttpStatusCode.Unknown
        )
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> NetworkResult<T>): NetworkResult<T> =
        try {
            call.invoke()
        } catch (e: Exception) {
            NetworkResult.Error(IOException(e))
        }

}