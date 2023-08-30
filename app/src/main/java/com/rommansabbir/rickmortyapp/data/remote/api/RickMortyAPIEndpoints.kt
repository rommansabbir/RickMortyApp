package com.rommansabbir.rickmortyapp.data.remote.api

import com.rommansabbir.rickmortyapp.base.api.APIEndpoints
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import retrofit2.Call
import retrofit2.http.GET


/**
 * Define all APIs for this demo project.
 *
 * Defined APIs are:
 * - Charters list
 * - Single character detail
 */
interface RickMortyAPIEndpoints {
    @GET(APIEndpoints.Characters.CharacterEndpoint)
    fun getCharactersListDefault(): Call<RickMortyCharactersListAPIResponse>
}