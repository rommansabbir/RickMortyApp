package com.rommansabbir.rickmortyapp.data.local.models

import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.storex.StoreAbleObject

/**
 * Caching request model which will be stored by StoreX
 * by extending [StoreAbleObject].
 *
 * @param paginatedURL Latest paginated url
 * @param list Latest all characters list
 */
data class CacheCharactersListRequestModel(
    val paginatedURL: String?,
    val list: MutableList<RickMortyCharactersListAPIResponse.Companion.Result>
) : StoreAbleObject()