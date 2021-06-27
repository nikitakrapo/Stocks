package com.nikitakrapo.android.stocks.di

import com.nikitakrapo.android.stocks.network.ApiKeyInterceptor
import com.nikitakrapo.android.stocks.network.EnumConverterFactory
import com.nikitakrapo.android.stocks.network.FinnhubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val apiKeyInterceptor = ApiKeyInterceptor()

        val okHttpClient =
            OkHttpClient()
                .newBuilder()
                .addInterceptor(apiKeyInterceptor)
                .build()

        return Retrofit.Builder()
            .baseUrl(FinnhubApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(EnumConverterFactory())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideFinnhubApiService(retrofit: Retrofit): FinnhubApiService {
        return retrofit.create(FinnhubApiService::class.java)
    }

}