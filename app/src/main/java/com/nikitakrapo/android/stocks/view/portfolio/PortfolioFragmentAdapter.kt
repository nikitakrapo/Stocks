package com.nikitakrapo.android.stocks.view.portfolio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PortfolioFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private val fragments = emptyList<PortfolioFragment>()

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}