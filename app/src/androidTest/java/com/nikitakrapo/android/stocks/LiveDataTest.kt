package com.nikitakrapo.android.stocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nikitakrapo.android.stocks.model.Result
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class LiveDataTest {

    @Test
    fun mapTest() {
        val _news =
            MarketNewsCategory.values().map { marketNewsCategory ->
                val mediatorLiveData =
                    MediatorLiveData<Result<List<MarketNewsArticle>>>()
                marketNewsCategory to mediatorLiveData
            }.toMap()

        val news =
            MarketNewsCategory.values().map { marketNewsCategory ->
                val liveData: LiveData<Result<List<MarketNewsArticle>>> =
                    _news[marketNewsCategory] as LiveData<Result<List<MarketNewsArticle>>>
                marketNewsCategory to liveData
            }.toMap()

        val result = Result.Success(
            listOf(
                MarketNewsArticle(
                    category = "general",
                    datetime = 0.toLong(),
                    headline = "headline",
                    id = 1,
                    related = "",
                    url = "",
                    image = "",
                    source = "",
                    summary = "test"
                )
            )
        )

        _news[MarketNewsCategory.GENERAL]!!.postValue(result)

        assertEquals(
            news[MarketNewsCategory.GENERAL]?.getOrAwaitValue(),
            result
        )
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        val liveData = this
        GlobalScope.launch(Dispatchers.Main) {
            liveData.observeForever(observer)
        }

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}