package com.rommansabbir.rickmortyapp.data.remote.repository

import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterDetailsAPIResponseModel

/**
 * Repository that is responsible to provide data for this demo application
 * from either remote or local.
 */
interface RickMortyRepository {
    /**
     * Get all Rick and Morty Characters (Paginated).
     *
     * @return [APIResult]<[RickMortyCharactersListAPIResponse]>
     */
    fun getAllCharacters(request: RickMortyCharactersListAPIRequest): APIResult<RickMortyCharactersListAPIResponse>

    /**
     * Get single character details.
     *
     * @return [APIResult]<[RickMortySingleCharacterDetailsAPIResponseModel]>
     */
    fun getSingleCharacterDetails(request: RickMortySingleCharacterAPIRequest): APIResult<RickMortySingleCharacterDetailsAPIResponseModel>
}