package com.rommansabbir.rickmortyapp.domain

import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.base.interactor.UseCase
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.local.repository.LocalCache
import javax.inject.Inject

class CacheCharactersListToLocalUseCase @Inject constructor(private val localCache: LocalCache) :
    UseCase<Boolean, CacheCharactersListRequestModel>() {
    override suspend fun run(params: CacheCharactersListRequestModel): APIResult<Boolean> =
        localCache.cacheCharactersListLocally(params)

}