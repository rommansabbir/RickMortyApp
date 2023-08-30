package com.rommansabbir.rickmortyapp.base.di.modules

import android.app.AppComponentFactory
import com.rommansabbir.rickmortyapp.BuildConfig.BASE_API_URL
import com.rommansabbir.storex.gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(AppComponentFactory::class)
@Module
object NetworkingModule {
    private val myHttpClient: OkHttpClient by lazy {
        val ins = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
        ins.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_API_URL).client(myHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}