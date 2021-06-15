package com.nikitakrapo.android.stocks.view.news

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
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentNewsBinding
import com.nikitakrapo.android.stocks.model.Result
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory
import com.nikitakrapo.android.stocks.repository.NewsRepository
import com.nikitakrapo.android.stocks.utils.ConnectionLiveData
import com.nikitakrapo.android.stocks.viewmodel.NewsViewModel
import com.nikitakrapo.android.stocks.viewmodel.GeneralNewsViewModelFactory

class NewsFragment : Fragment() {

    private lateinit var connectionLiveData: ConnectionLiveData
    
    private val newsViewModel: NewsViewModel by viewModels{
        GeneralNewsViewModelFactory(
            NewsRepository.getInstance(requireContext())
        )
    }

    private lateinit var recyclerView: RecyclerView

    companion object{
        private const val TAG = "NewsFragment"

        private const val MARKET_NEWS_CATEGORY_ARGS = "MARKET_NEWS_CATEGORY_ARGS"

        fun getInstance(marketNewsCategory: MarketNewsCategory): NewsFragment{
            Log.d(TAG, "getInstance $marketNewsCategory")
            return NewsFragment().apply {
                val args = Bundle()
                args.putSerializable(MARKET_NEWS_CATEGORY_ARGS, marketNewsCategory)
                arguments = args
            }
        }
    }

    private lateinit var marketNewsCategory: MarketNewsCategory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_news, container, false
        )

        marketNewsCategory =
            requireArguments().getSerializable(MARKET_NEWS_CATEGORY_ARGS) as MarketNewsCategory

        binding.newsViewModel = newsViewModel
        Log.d(TAG, marketNewsCategory.toString())
        binding.marketNewsCategory = marketNewsCategory

        recyclerView = binding.recyclerView

        val adapter = NewsAdapter()
        recyclerView.adapter = adapter

        binding.lifecycleOwner = this

        makeNewsCall()

        observeNews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireContext())
    }

    private fun observeNews(){
        newsViewModel.news[marketNewsCategory]?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Error -> {
                    TODO(it.exception.toString())
                }
                is Result.Success -> (recyclerView.adapter as NewsAdapter).submitList(it.data)
            }
        })
    }

    private fun makeNewsCall(){
        newsViewModel.makeNewsCall(marketNewsCategory)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}