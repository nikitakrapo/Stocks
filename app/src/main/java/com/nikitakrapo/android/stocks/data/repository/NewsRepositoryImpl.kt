package com.nikitakrapo.android.stocks.data.repository

import com.nikitakrapo.android.stocks.domain.model.HttpStatusCode
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.data.network.FinnhubApiService
import com.nikitakrapo.android.stocks.data.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.data.network.response.NewsArticleCategory
import com.nikitakrapo.android.stocks.data.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.data.cache.room.NewsCategoriesDao
import com.nikitakrapo.android.stocks.data.cache.room.NewsDao
import java.io.IOException

class NewsRepositoryImpl constructor(
    private val newsDao: NewsDao,
    private val newsCategoriesDao: NewsCategoriesDao,
    private val finnhubApiService: FinnhubApiService
) : NewsRepository {

    override fun addNews(marketNewsArticle: MarketNewsArticle, marketNewsCategory: MarketNewsCategory) {
        newsDao.addNews(marketNewsArticle)
        newsCategoriesDao.addNewsArticleWithCategory(
            NewsArticleCategory(marketNewsArticle.id, marketNewsCategory.value)
        )
    }

    override fun addNews(
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

    override fun getNewsByCategory(marketNewsCategory: MarketNewsCategory): List<MarketNewsArticle> {
        val ids = newsCategoriesDao.getIdsByCategory(marketNewsCategory)
        return newsDao.getNewsByIdsOrdered(ids)
    }

    override fun deleteAllNews() {
        newsDao.deleteAllNews()
        newsCategoriesDao.deleteAllNewsArticleWithCategories()
    }

    override fun replaceNews(
        marketNewsArticles: List<MarketNewsArticle>,
        marketNewsCategory: MarketNewsCategory
    ) {
        val ids = newsCategoriesDao.getIdsByCategory(marketNewsCategory)
        newsDao.deleteNewsByIds(ids)
        newsCategoriesDao.deleteByCategory(marketNewsCategory)
        addNews(marketNewsArticles, marketNewsCategory)
    }

    override suspend fun getMarketNews(newsCategory: MarketNewsCategory) = safeApiCall(
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