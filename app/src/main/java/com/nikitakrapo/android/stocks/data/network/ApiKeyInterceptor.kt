@file:Suppress("unused", "unused")

package com.nikitakrapo.android.stocks.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {

    companion object{
        private const val TAG = "ApiKeyInterceptor"
        private const val API_KEY = "c0vnvnn48v6t383llfgg"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url =
                original
                        .url()
                        .newBuilder()
                        .addQueryParameter("token", API_KEY)
                        .build()
        original =
                original
                        .newBuilder()
                        .url(url)
                        .build()
        return chain.proceed(original)
    }
}