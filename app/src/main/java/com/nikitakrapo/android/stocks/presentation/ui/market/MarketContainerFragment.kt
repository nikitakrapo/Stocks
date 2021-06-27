package com.nikitakrapo.android.stocks.presentation.ui.market

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentMarketContainerBinding

class MarketContainerFragment : Fragment() {

    companion object{
        private const val TAG = "MarketContainerFragment"
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabNames: List<String>

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentMarketContainerBinding>(
            inflater, R.layout.fragment_market_container, container, false
        )
        viewPager = binding.viewPager
        viewPager.adapter = MarketFragmentAdapter(this.requireActivity())

        tabLayout = binding.tabLayout

        initTabNames()

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return binding.root
    }

    fun initTabNames(){
        tabNames = listOf(
            getString(R.string.title_overview),
            getString(R.string.title_stocks)
        )
    }
}