package com.nikitakrapo.android.stocks.view.portfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentPortfolioBinding

class PortfolioFragment : Fragment() {

    companion object{
        fun getInstance(portfolioName: String): PortfolioFragment{
            return PortfolioFragment().also { it.portfolioName = portfolioName }
        }
    }

    var portfolioName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentPortfolioBinding>(
            inflater, R.layout.fragment_portfolio, container, false
        )

        return binding.root
    }
}