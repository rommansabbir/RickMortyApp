package com.rommansabbir.rickmortyapp.usecase

import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepositoryImpl
import com.rommansabbir.rickmortyapp.domain.GetRickMortyCharacterListUseCase
import com.rommansabbir.rickmortyapp.utils.RetrofitTestInstanceProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRickMortyCharacterListUseCaseTestCase {
    private var useCase: GetRickMortyCharacterListUseCase? = null

    @Before
    fun setup() {
        useCase = GetRickMortyCharacterListUseCase(
            RickMortyRepositoryImpl(RickMortyAPIService(RetrofitTestInstanceProvider.provideRetrofit()))
        )
    }

    @Test
    fun `make sure that use case is execute properly`() = runBlocking {
        val result = useCase?.let { it(RickMortyCharactersListAPIRequest(false, null)) }
        assert(result?.isError() == false)
    }

    @Test
    fun `get list of characters from the api`() = runBlocking {
        val result = useCase?.let { it(RickMortyCharactersListAPIRequest(false, null)) }
        assert(result?.isError() == false)
        assert(result?.asSuccess<RickMortyCharactersListAPIResponse>()?.results?.isEmpty() == false)
    }

    @Test
    fun `get list of characters from the api, paginated and should be unsuccessful`() =
        runBlocking {
            val result = useCase?.let {
                it(
                    RickMortyCharactersListAPIRequest(
                        false,
                        "https://rickandmortyapi.com/api/character/?page=998989"
                    )
                )
            }
            assert(result?.isError() == true)
        }

    @After
    fun tearDown() {
        useCase = null
    }
}