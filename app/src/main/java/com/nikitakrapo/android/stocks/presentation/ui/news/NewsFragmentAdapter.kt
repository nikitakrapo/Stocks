package com.nikitakrapo.android.stocks.presentation.ui.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.network.response.enums.MarketNewsCategory

class NewsFragmentAdapter(val fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private data class FragmentWithTabName(val fragment: NewsFragment, val tabName: String)

    fun getTabNameAt(position: Int): String{
        return fragmentsWithTabNames[position].tabName
    }

    private val fragmentsWithTabNames = listOf(
        FragmentWithTabName(
            NewsFragment.getInstance(MarketNewsCategory.GENERAL),
            fragment.getString(R.string.title_general_news)
        ),
        FragmentWithTabName(
            NewsFragment.getInstance(MarketNewsCategory.CRYPTO),
            fragment.getString(R.string.title_crypto_news)
        ),
        FragmentWithTabName(
            NewsFragment.getInstance(MarketNewsCategory.FOREX),
            fragment.getString(R.string.title_forex_news)
        ),
    )

    override fun getItemCount() = fragmentsWithTabNames.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsWithTabNames[position].fragment
    }
}