package com.nikitakrapo.android.stocks.view.news

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikitakrapo.android.stocks.databinding.NewsArticleItemBinding
import com.nikitakrapo.android.stocks.retrofit.FinnhubApiService.MarketNewsArticle

class NewsAdapter :
        ListAdapter<MarketNewsArticle, NewsAdapter.NewsViewHolder>(DiffCallback()){

    companion object{
        private const val TAG = "NewsAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class NewsViewHolder private constructor(val binding: NewsArticleItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: MarketNewsArticle) {
            binding.article = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NewsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NewsArticleItemBinding.inflate(layoutInflater, parent, false)
                return NewsViewHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MarketNewsArticle>() {
        override fun areItemsTheSame(oldItem: MarketNewsArticle, newItem: MarketNewsArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarketNewsArticle, newItem: MarketNewsArticle): Boolean {
            return oldItem == newItem
        }
    }
}