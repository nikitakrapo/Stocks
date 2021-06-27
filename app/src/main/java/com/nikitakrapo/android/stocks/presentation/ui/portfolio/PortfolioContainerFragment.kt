package com.nikitakrapo.android.stocks.presentation.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentPortfolioContainerBinding
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio

class PortfolioContainerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val portfolioViewModel: PortfolioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPortfolioContainerBinding>(
            inflater, R.layout.fragment_portfolio_container, container, false
        )

        viewPager = binding.viewPager
        viewPager.adapter = PortfolioFragmentAdapter(this.requireActivity())

        tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = (viewPager.adapter as PortfolioFragmentAdapter).getTabNameAt(position)
        }.attach()

        observePortfolios()

        return binding.root
    }

    private fun updateTabs(portfolioList: List<StockPortfolio>) {
        (viewPager.adapter as PortfolioFragmentAdapter).setTabs(
            portfolioList
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = (viewPager.adapter as PortfolioFragmentAdapter).getTabNameAt(position)
        }.attach()
    }

    private fun observePortfolios() {
        portfolioViewModel.portfolios.observe(viewLifecycleOwner, Observer { portfolioList ->
            updateTabs(portfolioList)
        })
    }
}