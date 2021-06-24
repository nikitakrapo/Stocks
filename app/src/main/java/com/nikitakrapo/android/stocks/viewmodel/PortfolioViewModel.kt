package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {

    lateinit var portfolios: LiveData<List<StockPortfolio>>

    init{
        getPortfolios()
    }

    fun addPortfolio(stockPortfolio: StockPortfolio){
        CoroutineScope(Dispatchers.IO).launch {
            portfolioRepository.addPortfolio(stockPortfolio)
        }
    }

    private fun getPortfolios(){
        portfolios = portfolioRepository.getPortfolios()
    }

}