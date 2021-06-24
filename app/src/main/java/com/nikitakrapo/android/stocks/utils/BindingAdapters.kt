package com.nikitakrapo.android.stocks.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapters {
    companion object{
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, articleImageUrl: String) {
            Glide.with(view.context)
                .load(articleImageUrl)
                .into(view)
        }
    }
}