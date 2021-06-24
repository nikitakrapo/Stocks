package com.nikitakrapo.android.stocks.view.portfolio

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.DialogAddPortfolioBinding
import com.nikitakrapo.android.stocks.databinding.FragmentPortfolioContainerBinding
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModel
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModelFactory

class PortfolioContainerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val portfolioViewModel: PortfolioViewModel by viewModels {
        PortfolioViewModelFactory(
            PortfolioRepository.getInstance(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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