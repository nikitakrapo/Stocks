package com.nikitakrapo.android.stocks.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.nikitakrapo.android.stocks.model.Result
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel(
        newsRepository: NewsRepository
) : ViewModel() {

    private var newsRepository: NewsRepository? = null

    private val _isRefreshing = MediatorLiveData<Boolean>()

    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _news =
        MarketNewsCategory.values().map{ marketNewsCategory ->
            val mediatorLiveData = MediatorLiveData<Result<List<MarketNewsArticle>>>()
            marketNewsCategory to mediatorLiveData
        }.toMap()

    val news =
        MarketNewsCategory.values().map{ marketNewsCategory ->
            val liveData: LiveData<Result<List<MarketNewsArticle>>> =
                _news[marketNewsCategory] as LiveData<Result<List<MarketNewsArticle>>>
            marketNewsCategory to liveData
        }.toMap()

    init {
        this.newsRepository = newsRepository
    }

    private var jobs =
        MarketNewsCategory.values().map { marketNewsCategory ->
            marketNewsCategory to (null as Job?)
        }.toMap().toMutableMap()

    fun makeNewsCall(marketNewsCategory: MarketNewsCategory) {
        _isRefreshing.postValue(true)
        jobs[marketNewsCategory] = CoroutineScope(IO).launch {
            val result =
                    newsRepository!!.getMarketNews(marketNewsCategory)
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