package com.nikitakrapo.android.stocks.view.portfolio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikitakrapo.android.stocks.model.StockPortfolio

class PortfolioFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private data class FragmentWithTabName(val fragment: PortfolioFragment, val tabName: String)

    fun getTabNameAt(position: Int): String{
        return fragmentsWithTabNames[position].tabName
    }

    fun setTabs(portfolios: List<StockPortfolio>){
        fragmentsWithTabNames = portfolios.map {
            FragmentWithTabName(
                PortfolioFragment.getInstance(it),
                it.name
            )
        }.toList()
    }

    private var fragmentsWithTabNames = listOf<FragmentWithTabName>()

    override fun getItemCount() = fragmentsWithTabNames.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsWithTabNames[position].fragment
    }
}