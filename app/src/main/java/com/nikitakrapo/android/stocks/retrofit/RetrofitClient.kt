package com.nikitakrapo.android.stocks.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {

            // Interceptor that adds api key to the request (...&token=*)
            val apiKeyInterceptor = ApiKeyInterceptor()

            val okHttpClient =
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(apiKeyInterceptor)
                    .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(EnumConverterFactory())
                .client(okHttpClient)
                .build()
        }
        return retrofit!!
    }
}