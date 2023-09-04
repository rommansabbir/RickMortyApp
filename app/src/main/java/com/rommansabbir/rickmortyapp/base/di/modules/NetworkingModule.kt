package com.rommansabbir.rickmortyapp.base.di.modules

import com.rommansabbir.rickmortyapp.BuildConfig
import com.rommansabbir.rickmortyapp.BuildConfig.BASE_API_URL
import com.rommansabbir.storex.gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkingModule {
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    private val myHttpClient: OkHttpClient by lazy {
        val ins = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            ins.addInterceptor(loggingInterceptor)
        }
        ins.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_API_URL).client(myHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}