package com.rommansabbir.rickmortyapp.utils

import com.rommansabbir.rickmortyapp.BuildConfig
import com.rommansabbir.storex.gson
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

object RetrofitTestInstanceProvider {
    private val myHttpClient: OkHttpClient by lazy {
        val ins = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
        ins.build()
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://rickandmortyapi.com/api/").client(myHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}