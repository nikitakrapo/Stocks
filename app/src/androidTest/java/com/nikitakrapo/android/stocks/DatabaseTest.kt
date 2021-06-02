package com.nikitakrapo.android.stocks

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.StockRepository
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.core.IsEqual
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var stockRepository: StockRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        stockRepository = StockRepository.getInstance(context)
    }

    // Also testing converter list<String> <-> String (json)
    @Test
    @Throws(Exception::class)
    fun addAndDeletePortfolio() {
        val portfolio: StockPortfolio =
                StockPortfolio(
                        "test",
                        listOf("AAPL", "BABA")
                )
        stockRepository.addPortfolio(portfolio)
        var dbPortfolio = stockRepository.getPortfolio("test").getOrAwaitValue()
        assertThat(dbPortfolio, IsEqual.equalTo(portfolio))
        stockRepository.deletePortfolio(dbPortfolio.name)
        dbPortfolio = stockRepository.getPortfolio("test").getOrAwaitValue()
        assertEquals(dbPortfolio, null)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllPortfolios() {
        val portfolio1: StockPortfolio =
                StockPortfolio(
                        "test",
                        listOf("AAPL", "BABA")
                )
        val portfolio2: StockPortfolio =
                StockPortfolio(
                        "test2",
                        listOf("AAPL11", "BABasdA")
                )
        stockRepository.addPortfolio(portfolio1)
        stockRepository.addPortfolio(portfolio2)
        var dbPortfolio1 = stockRepository.getPortfolio("test").getOrAwaitValue()
        var dbPortfolio2 = stockRepository.getPortfolio("test2").getOrAwaitValue()
        assertThat(dbPortfolio1, IsEqual.equalTo(portfolio1))
        assertThat(dbPortfolio2, IsEqual.equalTo(portfolio2))
        stockRepository.deleteAllPortfolios()
        dbPortfolio1 = stockRepository.getPortfolio("test").getOrAwaitValue()
        dbPortfolio2 = stockRepository.getPortfolio("test2").getOrAwaitValue()
        assertEquals(dbPortfolio1, null)
        assertEquals(dbPortfolio2, null)
    }

    @After
    fun clearDb() {
        stockRepository.deleteAllPortfolios()
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
        GlobalScope.launch(Main) {
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
