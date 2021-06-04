package com.nikitakrapo.android.stocks.viewmodel

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CryptoNewsViewModel(
    stockRepository: StockRepository
) : ViewModel() {

    private var stockRepository: StockRepository? = null

    private val _cryptoNews = MediatorLiveData<NetworkResult<List<FinnhubApiService.MarketNewsArticle>>>()
    private val _isRefreshing = MediatorLiveData<Boolean>()

    val cryptoNews: LiveData<NetworkResult<List<FinnhubApiService.MarketNewsArticle>>> get() = _cryptoNews
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    init {
        this.stockRepository = stockRepository
    }

    private var newsJob: Job? = null

    fun makeNewsCall() {
        _isRefreshing.postValue(true)
        newsJob = CoroutineScope(Dispatchers.IO).launch {
            val result =
                    stockRepository!!.getMarketNews(FinnhubApiService.MarketNewsCategory.CRYPTO)
            _cryptoNews.postValue(result)
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