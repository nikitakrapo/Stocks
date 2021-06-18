package com.nikitakrapo.android.stocks.repository

import android.content.Context
import com.nikitakrapo.android.stocks.model.HttpStatusCode
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.NewsArticleWithCategory
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import com.nikitakrapo.android.stocks.room.StockMarketDatabase
import java.io.IOException

class NewsRepository private constructor(context: Context){

    companion object {
        private lateinit var stockMarketDatabase: StockMarketDatabase
        private lateinit var finnhubApiService: FinnhubApiService

        private var repository: NewsRepository? = null

        fun getInstance(context: Context): NewsRepository{
            if (repository == null){
                repository = NewsRepository(context)
                stockMarketDatabase = StockMarketDatabase.getClient(context)
                finnhubApiService = FinnhubApiService.finnhubApiService
            }
            return repository!!
        }
    }

    fun addNews(marketNewsArticle: MarketNewsArticle, marketNewsCategory: MarketNewsCategory){
        stockMarketDatabase.newsDao().addNews(marketNewsArticle)
        stockMarketDatabase.newsCategoriesDao().addNewsArticleWithCategory(
            NewsArticleWithCategory(marketNewsArticle.id, marketNewsCategory.value)
        )
    }

    fun addNews(marketNewsArticles: List<MarketNewsArticle>, marketNewsCategory: MarketNewsCategory){
        stockMarketDatabase.newsDao().addNews(marketNewsArticles)
        stockMarketDatabase.newsCategoriesDao()
            .addNewsArticleWithCategory(
                marketNewsArticles.map {
                    NewsArticleWithCategory(it.id, marketNewsCategory.value)
                }.toList())
    }

    fun getNewsByCategory(marketNewsCategory: MarketNewsCategory): List<MarketNewsArticle>{
        val ids = stockMarketDatabase.newsCategoriesDao().getIdsByCategory(marketNewsCategory)
        return stockMarketDatabase.newsDao().getNewsByIdsOrdered(ids)
    }

    fun deleteAllNews(){
        stockMarketDatabase.newsDao().deleteAllNews()
        stockMarketDatabase.newsCategoriesDao().deleteAllNewsArticleWithCategories()
    }

    fun replaceNews(marketNewsArticles: List<MarketNewsArticle>, marketNewsCategory: MarketNewsCategory){
        val ids = stockMarketDatabase.newsCategoriesDao().getIdsByCategory(marketNewsCategory)
        stockMarketDatabase.newsDao().deleteNewsByIds(ids)
        stockMarketDatabase.newsCategoriesDao().deleteByCategory(marketNewsCategory)
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

    private suspend fun <T : Any> safeApiCall(call: suspend () -> NetworkResult<T>): NetworkResult<T> = try {
        call.invoke()
    } catch (e: Exception) {
        NetworkResult.Error(IOException(e))
    }

}