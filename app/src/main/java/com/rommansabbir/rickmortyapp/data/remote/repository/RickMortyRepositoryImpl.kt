package com.rommansabbir.rickmortyapp.data.remote.repository

import com.rommansabbir.rickmortyapp.base.api.client.executeAPIRequestV2
import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import javax.inject.Inject

/**
 * Implementation of [RickMortyRepository] where the [apiService] is provided via constructor
 * injection with the help of Hilt.
 */
class RickMortyRepositoryImpl @Inject constructor(private val apiService: RickMortyAPIService) :
    RickMortyRepository {

    override fun getAllCharacters(): APIResult<RickMortyCharactersListAPIResponse> =
        executeAPIRequestV2(
            apiService.getCharactersListDefault(), { it }, RickMortyCharactersListAPIResponse()
        )
}