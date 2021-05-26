package com.nikitakrapo.android.stocks.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {

            // Interceptor that adds api key Header to the request
            val apiKeyInterceptor = ApiKeyInterceptor()

            val okHttpClient =
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(apiKeyInterceptor)
                    .build()

            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}