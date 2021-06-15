package com.nikitakrapo.android.stocks

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.repository.NewsRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class NewsDatabaseTest {

    private lateinit var newsRepository: NewsRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        newsRepository = NewsRepository.getInstance(context)
    }

    @Test
    fun getNewsByCategoryTest(){
        val generalArticles = listOf(
            MarketNewsArticle(MarketNewsCategory.GENERAL.value,
                1000000,
                "Headline",
                1,
                "", "", "", "", ""),
            MarketNewsArticle(MarketNewsCategory.GENERAL.value,
                1002000,
                "Headline",
                3,
                "", "", "", "", ""),
            MarketNewsArticle(MarketNewsCategory.GENERAL.value,
                2000000,
                "Headline",
                2,
                "", "", "", "", "")
        )

        val cryptoArticles = listOf(
            MarketNewsArticle(MarketNewsCategory.CRYPTO.value,
                100240,
                "Headline",
                6,
                "", "", "", "", ""),
            MarketNewsArticle(MarketNewsCategory.CRYPTO.value,
                10052000,
                "Headline",
                8,
                "", "", "", "", ""),
            MarketNewsArticle(MarketNewsCategory.CRYPTO.value,
                22130000,
                "Headline",
                102,
                "", "", "", "", "")
        )

        val allArticles = generalArticles + cryptoArticles

        for (article in allArticles){
            newsRepository.addNewsArticle(article)
        }

        val actualGeneralArticles =
            newsRepository.getNewsByCategory(MarketNewsCategory.GENERAL).getOrAwaitValue()

        val actualCryptoArticles =
            newsRepository.getNewsByCategory(MarketNewsCategory.CRYPTO).getOrAwaitValue()

        assertEquals(generalArticles.sortedByDescending { it.datetime }, actualGeneralArticles)
        assertEquals(cryptoArticles.sortedByDescending { it.datetime }, actualCryptoArticles)
    }

    @After
    fun clearDb(){
        newsRepository.deleteAllNews()
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