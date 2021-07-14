package com.nikitakrapo.android.stocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio
import com.nikitakrapo.android.stocks.data.repository.PortfolioRepositoryImpl
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.core.IsEqual
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class PortfolioDatabaseTest {

    @Inject
    private lateinit var portfolioRepository: PortfolioRepositoryImpl

    // Also testing converter list<String> <-> String (json)
    @Test
    @Throws(Exception::class)
    fun addAndDeletePortfolio() {
        val portfolio =
            StockPortfolio(
                "test",
                mutableListOf("AAPL", "BABA")
            )
        portfolioRepository.addPortfolio(portfolio)
        var dbPortfolio = portfolioRepository.getPortfolio("test").getOrAwaitValue()
        assertThat(dbPortfolio, IsEqual.equalTo(portfolio))
        portfolioRepository.deletePortfolio(dbPortfolio.name)
        dbPortfolio = portfolioRepository.getPortfolio("test").getOrAwaitValue()
        assertEquals(dbPortfolio, null)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllPortfolios() {
        val portfolio1 =
            StockPortfolio(
                "test",
                mutableListOf("AAPL", "BABA")
            )
        val portfolio2 =
            StockPortfolio(
                "test2",
                mutableListOf("AAPL11", "BABasdA")
            )
        portfolioRepository.addPortfolio(portfolio1)
        portfolioRepository.addPortfolio(portfolio2)
        var dbPortfolio1 = portfolioRepository.getPortfolio("test").getOrAwaitValue()
        var dbPortfolio2 = portfolioRepository.getPortfolio("test2").getOrAwaitValue()
        assertThat(dbPortfolio1, IsEqual.equalTo(portfolio1))
        assertThat(dbPortfolio2, IsEqual.equalTo(portfolio2))
        portfolioRepository.deleteAllPortfolios()
        dbPortfolio1 = portfolioRepository.getPortfolio("test").getOrAwaitValue()
        dbPortfolio2 = portfolioRepository.getPortfolio("test2").getOrAwaitValue()
        assertEquals(dbPortfolio1, null)
        assertEquals(dbPortfolio2, null)
    }

    @After
    fun clearDb() {
        portfolioRepository.deleteAllPortfolios()
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
