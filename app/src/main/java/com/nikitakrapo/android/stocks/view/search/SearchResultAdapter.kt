package com.nikitakrapo.android.stocks.view.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikitakrapo.android.stocks.databinding.NewsArticleItemBinding
import com.nikitakrapo.android.stocks.databinding.SearchResultItemBinding
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.SymbolLookup.SingleSymbolLookup
import com.nikitakrapo.android.stocks.view.news.NewsAdapter
import com.nikitakrapo.android.stocks.view.news.NewsArticleActivity

class SearchResultAdapter :
    ListAdapter<SingleSymbolLookup, SearchResultAdapter.SearchResultViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SearchResultViewHolder private constructor(
        val binding: SearchResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SingleSymbolLookup) {
            Log.d(TAG, "bind")
            binding.singleSymbolLookup = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SearchResultViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchResultItemBinding.inflate(layoutInflater, parent, false)
                return SearchResultViewHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SingleSymbolLookup>() {
        override fun areItemsTheSame(oldItem: SingleSymbolLookup, newItem: SingleSymbolLookup): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: SingleSymbolLookup, newItem: SingleSymbolLookup): Boolean {
            return oldItem == newItem
        }
    }


    companion object{
        private const val TAG = "NewsAdapter"
    }
}