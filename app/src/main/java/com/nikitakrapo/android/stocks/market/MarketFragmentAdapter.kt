package com.nikitakrapo.android.stocks.market

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MarketFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        MarketOverviewFragment(),
        MarketStocksFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}