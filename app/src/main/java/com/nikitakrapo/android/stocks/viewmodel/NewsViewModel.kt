package com.nikitakrapo.android.stocks.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.MarketNewsCategory
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.MarketNewsArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel(
        stockRepository: StockRepository
) : ViewModel() {

    private var stockRepository: StockRepository? = null

    private val _isRefreshing = MediatorLiveData<Boolean>()

    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _news =
        MarketNewsCategory.values().map{ marketNewsCategory ->
            val mediatorLiveData = MediatorLiveData<NetworkResult<List<MarketNewsArticle>>>()
            marketNewsCategory to mediatorLiveData
        }.toMap()

    val news =
        MarketNewsCategory.values().map{ marketNewsCategory ->
            val liveData: LiveData<NetworkResult<List<MarketNewsArticle>>> =
                _news[marketNewsCategory] as LiveData<NetworkResult<List<MarketNewsArticle>>>
            marketNewsCategory to liveData
        }.toMap()

    init {
        this.stockRepository = stockRepository
    }

    private var jobs =
        MarketNewsCategory.values().map { marketNewsCategory ->
            marketNewsCategory to (null as Job?)
        }.toMap().toMutableMap()

    fun makeNewsCall(marketNewsCategory: MarketNewsCategory) {
        _isRefreshing.postValue(true)
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                    stockRepository!!.getMarketNews(marketNewsCategory)
            _news[marketNewsCategory]?.postValue(result)
            _isRefreshing.postValue(false)
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, profileImage: String) {
            Glide.with(view.context)
                    .load(profileImage)
                    .into(view)
        }
    }

    override fun onCleared() {
        super.onCleared()
        for (job in jobs)
            job.value?.cancel()
    }
}