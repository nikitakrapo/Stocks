package com.nikitakrapo.android.stocks.view.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NewsFragmentAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    private val fragments = listOf(
            GeneralNewsFragment(),
            CryptoNewsFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}