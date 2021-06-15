package com.nikitakrapo.android.stocks.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.nikitakrapo.android.stocks.model.Result
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
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

    fun addNewsArticle(marketNewsArticle: MarketNewsArticle){
        stockMarketDatabase.newsDao().addNewsArticle(marketNewsArticle)
    }

    fun getNewsByCategory(marketNewsCategory: MarketNewsCategory): LiveData<List<MarketNewsArticle>>{
        return stockMarketDatabase.newsDao().getNewsByCategory(marketNewsCategory)
    }

    fun deleteAllNews(){
        stockMarketDatabase.newsDao().deleteAllNews()
    }

    fun getAllNews(): LiveData<List<MarketNewsArticle>>{
        return stockMarketDatabase.newsDao().getAllNews()
    }

    suspend fun getMarketNews(newsCategory: MarketNewsCategory) = safeApiCall(
        call = { marketNews(newsCategory) }
    )

    private suspend fun marketNews(newsCategory: MarketNewsCategory): Result<List<MarketNewsArticle>> {
        val response = finnhubApiService.getMarketNews(newsCategory).execute()
        if (response.isSuccessful && response.body() != null) {
            if (response.body()!!.isEmpty()) {
                return Result.Error(Exception("No news"))
            }
            return Result.Success(response.body()!!)
        }
        return Result.Error(Exception("Unsuccessful response\""))
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> = try {
        call.invoke()
    } catch (e: Exception) {
        Result.Error(IOException(e))
    }

}