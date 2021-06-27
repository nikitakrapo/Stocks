package com.nikitakrapo.android.stocks.presentation.ui.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentNewsBinding
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.network.response.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.domain.utils.ConnectionLiveData
import com.nikitakrapo.android.stocks.domain.utils.ErrorTextUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private lateinit var connectionLiveData: ConnectionLiveData

    private var connectionLiveDataInitialized = false

    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentNewsBinding

    private lateinit var marketNewsCategory: MarketNewsCategory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news, container, false
        )
        marketNewsCategory =
            requireArguments().getSerializable(MARKET_NEWS_CATEGORY_ARGS) as MarketNewsCategory

        binding.lifecycleOwner = this
        binding.swipeRefresh.isRefreshing = true

        connectionLiveData = ConnectionLiveData(requireContext())

        binding.newsViewModel = newsViewModel
        binding.connectionLiveData = connectionLiveData
        binding.marketNewsCategory = marketNewsCategory

        recyclerView = binding.recyclerView

        val adapter = NewsAdapter()
        recyclerView.adapter = adapter

        observeNews()
        observeConnectionLiveData()

        return binding.root
    }

    private fun observeConnectionLiveData() {
        connectionLiveData.observe(viewLifecycleOwner, Observer { hasConnection ->
            Log.d(TAG, "$hasConnection")
            /*  Wait for connectionLiveData initialization,
            then, based on whether there is an Internet connection or not,
            take data through the ViewModel either from the network or from the database */
            if (!connectionLiveDataInitialized) {
                newsViewModel.refreshNews(marketNewsCategory, hasConnection)
                connectionLiveDataInitialized = true
            } else {
                // Update news when connection obtained
                if (hasConnection)
                    newsViewModel.refreshNews(marketNewsCategory, hasConnection)
            }
        })
    }

    private fun observeNews() {
        newsViewModel.news[marketNewsCategory]?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Error ->
                    showShortSnackbar(
                        ErrorTextUtils.getErrorSnackBarText(
                            it, requireContext()
                        )
                    )
                is NetworkResult.Success -> {
                    (recyclerView.adapter as NewsAdapter).submitList(it.data)
                }
            }

        })
    }

    private fun showShortSnackbar(text: String) {
        val contextView = binding.recyclerView
        Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val TAG = "NewsFragment"

        private const val MARKET_NEWS_CATEGORY_ARGS = "MARKET_NEWS_CATEGORY_ARGS"

        fun getInstance(marketNewsCategory: MarketNewsCategory): NewsFragment {
            return NewsFragment().apply {
                val args = Bundle()
                args.putSerializable(MARKET_NEWS_CATEGORY_ARGS, marketNewsCategory)
                arguments = args
            }
        }
    }
}