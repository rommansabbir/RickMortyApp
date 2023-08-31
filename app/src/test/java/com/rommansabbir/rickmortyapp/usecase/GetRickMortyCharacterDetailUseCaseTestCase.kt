package com.rommansabbir.rickmortyapp.usecase

import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterDetailsAPIResponseModel
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepositoryImpl
import com.rommansabbir.rickmortyapp.domain.GetRickMortyCharacterDetailUseCase
import com.rommansabbir.rickmortyapp.utils.RetrofitTestInstanceProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetRickMortyCharacterDetailUseCaseTestCase {
    private var useCase: GetRickMortyCharacterDetailUseCase? = null

    @Before
    fun setup() {
        useCase = GetRickMortyCharacterDetailUseCase(
            RickMortyRepositoryImpl(RickMortyAPIService(RetrofitTestInstanceProvider.provideRetrofit()))
        )
    }

    @Test
    fun `make sure that use case is execute properly`() = runBlocking {
        val result = useCase?.let { it(RickMortySingleCharacterAPIRequest(1)) }
        assert(result?.isError() == false)
    }

    @Test
    fun `get character detail from the api`() = runBlocking {
        val result = useCase?.let { it(RickMortySingleCharacterAPIRequest(1)) }
        assert(result?.isError() == false)
        assert(result?.asSuccess<RickMortySingleCharacterDetailsAPIResponseModel>()?.id != null)
    }

    @Test
    fun `get character detail and should be unsuccessful`() =
        runBlocking {
            val result = useCase?.let {
                it(
                    RickMortySingleCharacterAPIRequest(5445645)
                )
            }
            assert(result?.isError() == true)
        }

    @After
    fun tearDown() {
        useCase = null
    }
}