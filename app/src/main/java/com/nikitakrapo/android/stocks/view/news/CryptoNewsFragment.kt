package com.nikitakrapo.android.stocks.view.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentCryptoNewsBinding
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.viewmodel.CryptoNewsViewModel
import com.nikitakrapo.android.stocks.viewmodel.CryptoNewsViewModelFactory


class CryptoNewsFragment : Fragment() {

    private val adapter = NewsAdapter()

    private lateinit var cryptoNewsViewModel: CryptoNewsViewModel

    companion object{
        private const val TAG = "CryptoNewsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCryptoNewsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_crypto_news, container, false
        )

        val dataSource = StockRepository.getInstance(requireContext())
        val viewModelFactory = CryptoNewsViewModelFactory(dataSource)

        cryptoNewsViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(CryptoNewsViewModel::class.java)

        binding.cryptoNewsViewModel = cryptoNewsViewModel

        val adapter = NewsAdapter()
        binding.recyclerView.adapter = adapter

        binding.lifecycleOwner = this

        makeNewsCall()
        cryptoNewsViewModel.cryptoNews.observe(viewLifecycleOwner, Observer { networkResult ->
            Log.d(TAG, "result: $networkResult")
            when(networkResult){
                is NetworkResult.Success -> adapter.submitList(networkResult.data)
                is NetworkResult.Error -> TODO()
            }
        })

        return binding.root
    }

    fun makeNewsCall(){
        cryptoNewsViewModel.makeNewsCall()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}