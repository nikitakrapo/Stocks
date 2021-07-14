package com.nikitakrapo.android.stocks.presentation.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.data.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.data.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _news =
        MarketNewsCategory.values().map { marketNewsCategory ->
            val mutableStateFlow =
                MutableStateFlow<NetworkResult<List<MarketNewsArticle>>>(
                    NetworkResult.Success(emptyList())
                )
            marketNewsCategory to mutableStateFlow
        }.toMap()

    val news =
        MarketNewsCategory.values().map { marketNewsCategory ->
            val stateFlow: StateFlow<NetworkResult<List<MarketNewsArticle>>> =
                _news[marketNewsCategory]!!.asStateFlow()
            marketNewsCategory to stateFlow
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
        _isRefreshing.value = true
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                newsRepository.getNewsByCategory(marketNewsCategory)
            _news[marketNewsCategory]!!.value = NetworkResult.Success(result)
            _isRefreshing.value = false
        }
    }

    private fun makeNewsCall(marketNewsCategory: MarketNewsCategory) {
        _isRefreshing.value = true
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                newsRepository.getMarketNews(marketNewsCategory)
            _news[marketNewsCategory]!!.value = result
            _isRefreshing.value = false
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