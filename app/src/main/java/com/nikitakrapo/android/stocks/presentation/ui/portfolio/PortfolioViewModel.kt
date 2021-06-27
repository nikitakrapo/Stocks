package com.nikitakrapo.android.stocks.presentation.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
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