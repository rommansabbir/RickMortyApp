package com.rommansabbir.rickmortyapp.domain

import com.rommansabbir.rickmortyapp.base.appresult.AppResult
import com.rommansabbir.rickmortyapp.base.interactor.UseCase
import com.rommansabbir.rickmortyapp.data.local.repository.LocalCache
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import javax.inject.Inject

class GetCharactersListFromLocalUseCase @Inject constructor(private val localCache: LocalCache) :
    UseCase<RickMortyCharactersListAPIResponse, UseCase.None>() {
    override suspend fun run(params: None): AppResult<RickMortyCharactersListAPIResponse> =
        localCache.getCharactersListFromLocal()
}