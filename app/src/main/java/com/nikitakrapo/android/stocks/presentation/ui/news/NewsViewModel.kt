package com.nikitakrapo.android.stocks.presentation.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _isRefreshing = MediatorLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _news =
        MarketNewsCategory.values().map { marketNewsCategory ->
            val mediatorLiveData = MediatorLiveData<NetworkResult<List<MarketNewsArticle>>>()
            marketNewsCategory to mediatorLiveData
        }.toMap()

    val news =
        MarketNewsCategory.values().map { marketNewsCategory ->
            val liveData: LiveData<NetworkResult<List<MarketNewsArticle>>> =
                _news[marketNewsCategory] as LiveData<NetworkResult<List<MarketNewsArticle>>>
            marketNewsCategory to liveData
        }.toMap()

    private var jobs =
        MarketNewsCategory.values().map { marketNewsCategory ->
            marketNewsCategory to (null as Job?)
        }.toMap().toMutableMap()

    fun refreshNews(marketNewsCategory: MarketNewsCategory, hasConnection: Boolean) {
        if (hasConnection)
            makeNewsCall(marketNewsCategory)
        else
            getNewsFromDb(marketNewsCategory)
        Log.d(
            TAG,
            "hasConnection: $hasConnection)"
        )
    }

    private fun getNewsFromDb(marketNewsCategory: MarketNewsCategory) {
        _isRefreshing.postValue(true)
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                newsRepository.getNewsByCategory(marketNewsCategory)
            _news[marketNewsCategory]?.postValue(NetworkResult.Success(result))
            _isRefreshing.postValue(false)
        }
    }

    private fun makeNewsCall(marketNewsCategory: MarketNewsCategory) {
        _isRefreshing.postValue(true)
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                newsRepository.getMarketNews(marketNewsCategory)
            _news[marketNewsCategory]?.postValue(result)
            _isRefreshing.postValue(false)
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        for (job in jobs)
            job.value?.cancel()
    }
}