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
import com.nikitakrapo.android.stocks.databinding.FragmentGeneralNewsBinding
import com.nikitakrapo.android.stocks.model.NetworkResult
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.viewmodel.GeneralNewsViewModel
import com.nikitakrapo.android.stocks.viewmodel.GeneralNewsViewModelFactory

class GeneralNewsFragment : Fragment() {

    private val adapter = NewsAdapter()

    private lateinit var generalNewsViewModel: GeneralNewsViewModel

    companion object{
        private const val TAG = "GeneralNewsFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGeneralNewsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_general_news, container, false
        )

        val dataSource = StockRepository.getInstance(requireContext())
        val viewModelFactory = GeneralNewsViewModelFactory(dataSource)

        generalNewsViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(GeneralNewsViewModel::class.java)

        binding.generalNewsViewModel = generalNewsViewModel

        val adapter = NewsAdapter()
        binding.recyclerView.adapter = adapter

        binding.lifecycleOwner = this

        makeNewsCall()
        generalNewsViewModel.generalNews.observe(viewLifecycleOwner, Observer { networkResult ->
            Log.d(TAG, "result: $networkResult")
            when(networkResult){
                is NetworkResult.Success -> adapter.submitList(networkResult.data)
                is NetworkResult.Error -> TODO()
            }
        })

        return binding.root
    }

    fun makeNewsCall(){
        generalNewsViewModel.makeNewsCall()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}