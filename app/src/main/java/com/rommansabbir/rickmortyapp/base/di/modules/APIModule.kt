package com.rommansabbir.rickmortyapp.base.di.modules

import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepository
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object APIModule {
    @Provides
    fun provideRickMortyRepository(apiService: RickMortyAPIService): RickMortyRepository =
        RickMortyRepositoryImpl(apiService)
}