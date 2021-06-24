package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.repository.PortfolioRepository

class PortfolioViewModelFactory(
    private val portfolioRepository: PortfolioRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            return PortfolioViewModel(portfolioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}