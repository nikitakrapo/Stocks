package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.PortfolioRepository

class PortfolioViewModel(
    portfolioRepository: PortfolioRepository
) : ViewModel() {

    private var portfolioRepository: PortfolioRepository? = null

    lateinit var portfolios: LiveData<List<StockPortfolio>>

    init{
        this.portfolioRepository = portfolioRepository
        getPortfolios()
    }

    private fun getPortfolios(){
        portfolios = portfolioRepository!!.getPortfolios()
    }

}