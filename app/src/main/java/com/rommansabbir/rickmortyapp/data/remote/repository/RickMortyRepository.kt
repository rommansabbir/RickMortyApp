package com.rommansabbir.rickmortyapp.data.remote.repository

import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse

/**
 * Repository that is responsible to provide data for this demo application
 * from either remote or local.
 */
interface RickMortyRepository {
    /**
     * Get all Rick and Morty Characters.
     *
     * @return [APIResult]<[RickMortyCharactersListAPIResponse]>
     */
    fun getAllCharacters(): APIResult<RickMortyCharactersListAPIResponse>
}