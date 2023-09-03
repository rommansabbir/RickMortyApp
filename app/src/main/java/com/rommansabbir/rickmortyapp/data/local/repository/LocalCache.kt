package com.rommansabbir.rickmortyapp.data.local.repository

import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse

/**
 * Responsible to cache data locally for this application.
 */
interface LocalCache {
    /**
     * Cache new state to the local cache.
     * If any error occurs during the cache return FailedToCache failure.
     *
     * @param requestModel Model to be cached.
     *
     * @return [APIResult]<[Boolean]>.
     */
    fun cacheCharactersListLocally(requestModel: CacheCharactersListRequestModel): APIResult<Boolean>

    /**
     * Get cached characters list from the local.
     * If found, parse the cached to the api response model
     * or if not found or error occurs return NotExistInCache failure.
     *
     * @return [APIResult]<[RickMortyCharactersListAPIResponse]>
     */
    fun getCharactersListFromLocal(): APIResult<RickMortyCharactersListAPIResponse>
}