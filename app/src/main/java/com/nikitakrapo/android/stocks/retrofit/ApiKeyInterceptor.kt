package com.nikitakrapo.android.stocks.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {

    companion object{
        private const val API_KEY = "c0vnvnn48v6t383llfgg"
        private const val API_KEY_NAME = "token"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val newRequest: Request = request.newBuilder()
            .addHeader(API_KEY_NAME, API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}