package com.rommansabbir.rickmortyapp.api

import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.utils.RetrofitTestInstanceProvider
import org.junit.After
import org.junit.Before
import org.junit.Test

class APIEndpointsTestCases {
    private var apiService: RickMortyAPIService? = null
    private val retrofit = RetrofitTestInstanceProvider.provideRetrofit()

    @Before
    fun setup() {
        apiService = RickMortyAPIService(retrofit = retrofit)
    }

    @Test
    fun `check the base url matches to the rick and morty base url`() {
        assert(retrofit.baseUrl().toUrl().toString() == "https://rickandmortyapi.com/api/")
    }

    @Test
    fun `api service instance should not be null`() {
        assert(apiService != null)
    }

    @Test
    fun `get character list from api and should be successful`() {
        val result = apiService?.getCharactersListDefault()?.execute()
        assert(result != null)
        assert(result?.isSuccessful == true && result.code() == 200)
        assert(result?.body() != null)
        assert(result?.body()?.results?.isEmpty() == false)
    }

    @Test
    fun `get character list from api, should be successful and paginated`() {
        val result =
            apiService?.getCharactersListPaginated("https://rickandmortyapi.com/api/character/?page=2")
                ?.execute()
        assert(result != null)
        assert(result?.isSuccessful == true && result.code() == 200)
        assert(result?.body() != null)
        assert(result?.body()?.results?.isEmpty() == false)
    }

    @Test
    fun `get character list from api, should be unsuccessful and paginated`() {
        val result =
            apiService?.getCharactersListPaginated("https://rickandmortyapi.com/api/character/?page=998989")
                ?.execute()
        assert(result?.isSuccessful == false && result.code() != 200)
    }

    @Test
    fun `get single character detail from the api and should be successful`() {
        val result = apiService?.getCharacterDetail(1)?.execute()
        assert(result != null)
        assert(result?.isSuccessful == true && result.code() == 200)
        assert(result?.body() != null)
        assert(result?.body()?.id != null)
    }

    @Test
    fun `get single character detail from the api and should be unsuccessful`() {
        val result = apiService?.getCharacterDetail(999)?.execute()
        assert(result != null)
        assert(result?.isSuccessful == false && result.code() != 200)
        assert(result?.body() == null)
    }


    @After
    fun tearDown() {
        apiService = null
    }
}