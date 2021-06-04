package com.nikitakrapo.android.stocks.viewmodel

import android.app.Application
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.MarketNewsArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GeneralNewsViewModel(
        stockRepository: StockRepository
) : ViewModel() {

    private var stockRepository: StockRepository? = null

    private val _generalNews = MediatorLiveData<NetworkResult<List<MarketNewsArticle>>>()
    private val _isRefreshing = MediatorLiveData<Boolean>()

    val generalNews: LiveData<NetworkResult<List<MarketNewsArticle>>> get() = _generalNews
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    init {
        this.stockRepository = stockRepository
    }

    private var newsJob: Job? = null

    fun makeNewsCall() {
        _isRefreshing.postValue(true)
        newsJob = CoroutineScope(IO).launch {
            val result =
                    stockRepository!!.getMarketNews(FinnhubApiService.MarketNewsCategory.GENERAL)
            _generalNews.postValue(result)
            _isRefreshing.postValue(false)
        }
    }

    companion object {
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
        newsJob?.cancel()
    }
}