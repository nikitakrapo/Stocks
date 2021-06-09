package com.nikitakrapo.android.stocks.view.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentPortfolioContainerBinding
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModel
import com.nikitakrapo.android.stocks.viewmodel.PortfolioViewModelFactory

class PortfolioContainerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val portfolioViewModel: PortfolioViewModel by viewModels{
        PortfolioViewModelFactory(
            PortfolioRepository.getInstance(requireContext())
        )
    }

    private var tabNames: List<String> = emptyList()

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

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePortfolios()
    }

    private fun observePortfolios(){

    }
}