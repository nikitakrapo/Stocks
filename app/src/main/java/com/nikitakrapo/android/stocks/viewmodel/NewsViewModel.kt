package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.MarketNewsArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private var stockRepository: StockRepository? = null

    private val _news = MediatorLiveData<List<MarketNewsArticle>>()
    private val _error = MediatorLiveData<String>()

    val news: LiveData<List<MarketNewsArticle>> get() = _news
    val error : LiveData<String> get() = _error

    private var newsJob: Job? = null

    init {
        getNewsCall()
    }

    private fun getNewsCall() {
        newsJob = CoroutineScope(IO).launch {
            val result = stockRepository!!.getMarketNews(FinnhubApiService.MarketNewsCategory.GENERAL)
            when (result) {
                is NetworkResult.Success -> _news.postValue(result.data)
                is NetworkResult.Error -> _error.postValue(result.exception.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        newsJob?.cancel()
    }
}