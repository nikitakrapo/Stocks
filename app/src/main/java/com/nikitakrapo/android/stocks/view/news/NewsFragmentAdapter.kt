package com.nikitakrapo.android.stocks.view.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory

class NewsFragmentAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    // Should match with fragments' names
    fun getTabNamesResourceIds(): List<Int>{
        return listOf(
            R.string.title_general_news,
            R.string.title_crypto_news,
            R.string.title_forex_news
        )
    }

    // Should match with tabs' names
    private val fragments = listOf(
        NewsFragment.getInstance(MarketNewsCategory.GENERAL),
        NewsFragment.getInstance(MarketNewsCategory.CRYPTO),
        NewsFragment.getInstance(MarketNewsCategory.FOREX)
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}