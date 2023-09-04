package com.rommansabbir.rickmortyapp.base.di

import android.app.Application
import com.rommansabbir.networkx.NetworkXLifecycle
import com.rommansabbir.networkx.NetworkXProvider
import com.rommansabbir.networkx.SmartConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickMortyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkXProvider.enable(SmartConfig(this, false, NetworkXLifecycle.Application))
    }
}