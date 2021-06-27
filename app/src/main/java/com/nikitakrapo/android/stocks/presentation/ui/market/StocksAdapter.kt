package com.nikitakrapo.android.stocks.presentation.ui.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikitakrapo.android.stocks.databinding.StockListItemBinding
import com.nikitakrapo.android.stocks.domain.model.Stock

class StocksAdapter :
    ListAdapter<Stock, StocksAdapter.StockViewHolder>(DiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class StockViewHolder private constructor(val binding: StockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Stock) {
            binding.stock = item
        }

        companion object {
            fun from(parent: ViewGroup): StockViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StockListItemBinding.inflate(layoutInflater, parent, false)
                return StockViewHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(
            oldItem: Stock,
            newItem: Stock
        ): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(
            oldItem: Stock,
            newItem: Stock
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val TAG = "StockAdapter"
    }
}