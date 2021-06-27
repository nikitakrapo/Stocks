package com.nikitakrapo.android.stocks.presentation.ui.news

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContextWrapper
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.NewsArticleItemBinding
import com.nikitakrapo.android.stocks.network.response.MarketNewsArticle

class NewsAdapter :
    ListAdapter<MarketNewsArticle, NewsAdapter.NewsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class NewsViewHolder private constructor(val binding: NewsArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarketNewsArticle) {
            binding.article = item
            binding.constraintLayout.setOnClickListener {
                startNewsArticleActivity(item)
            }
            binding.executePendingBindings()
        }

        private fun startNewsArticleActivity(marketNewsArticle: MarketNewsArticle) {
            val context = binding.root.context
            val intent = Intent(context, NewsArticleActivity::class.java)
            intent.putExtra(NewsArticleActivity.ARTICLE_EXTRA, marketNewsArticle)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                (binding.root.context as ContextWrapper).baseContext as Activity,
                Pair(binding.articleImage, context.getString(R.string.shared_news_article_image)),
                Pair(binding.articleTitle, context.getString(R.string.shared_news_article_headline))
            )
            context.startActivity(intent, options.toBundle())
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
        override fun areItemsTheSame(
            oldItem: MarketNewsArticle,
            newItem: MarketNewsArticle
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MarketNewsArticle,
            newItem: MarketNewsArticle
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TAG = "NewsAdapter"
    }
}