package com.rommansabbir.rickmortyapp.data.remote.api

import com.rommansabbir.rickmortyapp.base.api.APIEndpoints
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterDetailsAPIResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


/**
 * Define all APIs for this demo project.
 *
 * Defined APIs are:
 * - Charters list (Default, Paginated).
 * - Single character detail.
 */
interface RickMortyAPIEndpoints {
    /**
     * Get default characters list from the api.
     *
     * @return [RickMortyCharactersListAPIResponse] upon on success.
     */
    @GET(APIEndpoints.Characters.CharacterEndpoint)
    fun getCharactersListDefault(): Call<RickMortyCharactersListAPIResponse>

    /**
     * Get characters list from the api (Paginated).
     *
     * @param url Paginated URL
     *
     * @return [RickMortyCharactersListAPIResponse] upon on success.
     */
    @GET
    fun getCharactersListPaginated(
        @Url url: String
    ): Call<RickMortyCharactersListAPIResponse>


    /**
     * Get single character detail from the api.
     *
     * @return [RickMortySingleCharacterDetailsAPIResponseModel] upon on success.
     */
    @GET("${APIEndpoints.Characters.CharacterEndpoint}/{id}")
    fun getCharacterDetail(
        @Path("id") id: Int
    ): Call<RickMortySingleCharacterDetailsAPIResponseModel>
}