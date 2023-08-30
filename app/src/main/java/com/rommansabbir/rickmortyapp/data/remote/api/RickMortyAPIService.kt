package com.rommansabbir.rickmortyapp.data.remote.api

import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Implementation of the [RickMortyAPIEndpoints].
 *
 * The instance of [Retrofit] will be provided by the Hilt through constructor injection.
 *
 * [apiEndpoints] marked lazy to be initialized on-demand which help in terms of memory
 * management in larger scale project.
 */
class RickMortyAPIService @Inject constructor(private val retrofit: Retrofit) :
    RickMortyAPIEndpoints {
    private val apiEndpoints by lazy { retrofit.create(RickMortyAPIEndpoints::class.java) }

    override fun getCharactersListDefault(): Call<RickMortyCharactersListAPIResponse> =
        apiEndpoints.getCharactersListDefault()
}