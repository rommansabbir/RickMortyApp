package com.rommansabbir.rickmortyapp.data.remote.repository

import com.rommansabbir.rickmortyapp.base.appresult.AppResult
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
     * @return [AppResult]<[RickMortyCharactersListAPIResponse]>
     */
    fun getAllCharacters(request: RickMortyCharactersListAPIRequest): AppResult<RickMortyCharactersListAPIResponse>

    /**
     * Get single character details.
     *
     * @return [AppResult]<[RickMortySingleCharacterDetailsAPIResponseModel]>
     */
    fun getSingleCharacterDetails(request: RickMortySingleCharacterAPIRequest): AppResult<RickMortySingleCharacterDetailsAPIResponseModel>
}