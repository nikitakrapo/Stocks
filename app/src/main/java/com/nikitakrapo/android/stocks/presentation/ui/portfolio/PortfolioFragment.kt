package com.nikitakrapo.android.stocks.presentation.ui.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentPortfolioBinding
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio

class PortfolioFragment : Fragment() {

    companion object{

        private const val TAG = "PortfolioFragment"

        private const val PORTFOLIO_FRAGMENT_PORTFOLIO = "PORTFOLIO_FRAGMENT_PORTFOLIO"

        fun getInstance(stockPortfolio: StockPortfolio): PortfolioFragment {
            return PortfolioFragment().apply {
                val args = Bundle()
                args.putSerializable(PORTFOLIO_FRAGMENT_PORTFOLIO, stockPortfolio)
                arguments = args
            }
        }
    }

    private lateinit var stockPortfolio: StockPortfolio

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentPortfolioBinding>(
            inflater, R.layout.fragment_portfolio, container, false
        )

        stockPortfolio =
            requireArguments().getSerializable(PORTFOLIO_FRAGMENT_PORTFOLIO) as StockPortfolio

        binding.stockPortfolio = stockPortfolio

        return binding.root
    }
}