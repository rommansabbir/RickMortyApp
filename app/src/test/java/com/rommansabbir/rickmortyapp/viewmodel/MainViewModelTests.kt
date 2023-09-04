package com.rommansabbir.rickmortyapp.viewmodel

import androidx.test.core.app.ApplicationProvider
import com.rommansabbir.rickmortyapp.base.failure.Failure
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.local.repository.LocalCache
import com.rommansabbir.rickmortyapp.data.local.repository.LocalCacheImpl
import com.rommansabbir.rickmortyapp.data.remote.api.RickMortyAPIService
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterDetailsAPIResponseModel
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepository
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepositoryImpl
import com.rommansabbir.rickmortyapp.domain.CacheCharactersListToLocalUseCase
import com.rommansabbir.rickmortyapp.domain.GetCharactersListFromLocalUseCase
import com.rommansabbir.rickmortyapp.domain.GetRickMortyCharacterDetailUseCase
import com.rommansabbir.rickmortyapp.domain.GetRickMortyCharacterListUseCase
import com.rommansabbir.rickmortyapp.feature.MainViewModel
import com.rommansabbir.rickmortyapp.utils.RetrofitTestInstanceProvider
import com.rommansabbir.rickmortyapp.utils.extensions.executeBodyOrReturnNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainViewModelTests {
    private lateinit var vm: MainViewModel
    private lateinit var apiService: RickMortyAPIService
    private lateinit var repository: RickMortyRepository
    private lateinit var localCache: LocalCache

    @Before
    fun setup() {
        apiService = RickMortyAPIService(RetrofitTestInstanceProvider.provideRetrofit())
        repository = RickMortyRepositoryImpl(apiService)
        localCache = LocalCacheImpl(ApplicationProvider.getApplicationContext())
        vm = MainViewModel(
            GetRickMortyCharacterListUseCase(repository),
            CacheCharactersListToLocalUseCase(localCache),
            GetCharactersListFromLocalUseCase(localCache),
            GetRickMortyCharacterDetailUseCase(repository)
        )
    }

    @Test
    fun `test first run should be false and should be updated properly`() {
        assert(!vm.isFirstRun)
        vm.isFirstRun = true
        assert(vm.isFirstRun)
    }

    @Test
    fun `fetch list of characters from the remote source, should be successful`() = runBlocking {
        val request = RickMortyCharactersListAPIRequest(false, null)
        val result = vm.getCharacterListFromRemote(request)
        assert(!result.isError())
        val apiResponse =
            executeBodyOrReturnNull { result.asSuccess<RickMortyCharactersListAPIResponse>() }
        assert(apiResponse != null)
        assert(apiResponse?.results?.size != 0)
    }

    @Test
    fun `fetch list of characters from the remote source, test should fail because of character list not found`() =
        runBlocking {
            val request = RickMortyCharactersListAPIRequest(
                false,
                "https://rickandmortyapi.com/api/character/?page=56456456"
            )
            val result = vm.getCharacterListFromRemote(request)
            assert(result.isError())
            assert(result.asFailure() is Failure.HTTP.NotFound)
        }

    @Test
    fun `fetch characters detail the remote source, should be successful`() = runBlocking {
        val idToFindDetail = 1
        val request = RickMortySingleCharacterAPIRequest(idToFindDetail)
        val result = vm.getCharacterDetailRemote(request)
        assert(!result.isError())
        val apiResponse =
            executeBodyOrReturnNull { result.asSuccess<RickMortySingleCharacterDetailsAPIResponseModel>() }
        assert(apiResponse != null)
        assert(apiResponse?.id == idToFindDetail)
    }

    @Test
    fun `fetch characters detail the remote source, test should fail because of character not found`() =
        runBlocking {
            val idToFindDetail = 99996
            val request = RickMortySingleCharacterAPIRequest(idToFindDetail)
            val result = vm.getCharacterDetailRemote(request)
            assert(result.isError())
            assert(result.asFailure() is Failure.HTTP.NotFound)
        }

    @Test
    fun `fetch list of characters from the remote source with pagination for next 10 request, should be successful`() =
        runBlocking {
            var totalCountedTime = 0
            var nextPaginatedURL: String? = null
            val limit = 10
            for (count in 0..limit) runBlocking {
                val request = RickMortyCharactersListAPIRequest(false, nextPaginatedURL)
                val result = vm.getCharacterListFromRemote(request)
                assert(!result.isError())
                val apiResponse =
                    executeBodyOrReturnNull { result.asSuccess<RickMortyCharactersListAPIResponse>() }
                assert(apiResponse != null)
                assert(apiResponse?.results?.size != 0)
                nextPaginatedURL = apiResponse?.paginationInfo?.next
                totalCountedTime = count
            }
            assert(limit == totalCountedTime)
        }

    @Test
    fun `fetch list of characters from the remote source and cache locally, should be successful`() =
        runBlocking {
            val request = RickMortyCharactersListAPIRequest(false, null)
            val result = vm.getCharacterListFromRemote(request)
            assert(!result.isError())
            val apiResponse =
                executeBodyOrReturnNull { result.asSuccess<RickMortyCharactersListAPIResponse>() }
            assert(apiResponse != null)
            assert(apiResponse?.results?.size != 0)
            val cachingSuccess = vm.cacheCharactersListToLocal(
                CacheCharactersListRequestModel(
                    apiResponse?.paginationInfo?.next, apiResponse?.results ?: mutableListOf()
                )
            )
            assert(!cachingSuccess.isError())
        }

    @Test
    fun `fetch cached characters list from local, should be successful`() = runBlocking {
        val url = "https://rickandmortyapi.com/api/character/?page=2"
        val list = mutableListOf<RickMortyCharactersListAPIResponse.Companion.Result>().apply {
            add(RickMortyCharactersListAPIResponse.Companion.Result().apply {
                id = 0
                name = "Test Name"
            })
            add(RickMortyCharactersListAPIResponse.Companion.Result().apply {
                id = 1
                name = "Test Name 1"
            })
            add(RickMortyCharactersListAPIResponse.Companion.Result().apply {
                id = 2
                name = "Test Name 2"
            })
        }
        val cachingSuccess =
            vm.cacheCharactersListToLocal(CacheCharactersListRequestModel(url, list))
        assert(!cachingSuccess.isError())
        val cachedResult = vm.getCharactersListFromLocal()
        assert(!cachedResult.isError())
        val data =
            executeBodyOrReturnNull { cachedResult.asSuccess<RickMortyCharactersListAPIResponse>() }
        assert(data != null)
        assert(data?.results?.size == list.size)
        assert(data?.paginationInfo?.next == url)
    }

    @Test
    fun `fetch list of characters from the remote source and update the data in the respective ui state, should be successful`() =
        runBlocking {
            val request = RickMortyCharactersListAPIRequest(false, null)
            val result = vm.getCharacterListFromRemote(request)
            assert(!result.isError())
            val apiResponse =
                executeBodyOrReturnNull { result.asSuccess<RickMortyCharactersListAPIResponse>() }
            assert(apiResponse != null)
            assert(apiResponse?.results?.size != 0)
            vm.mapAPIResponseToUIState(result)
            assert(vm.characterListUIState.dataList.size == apiResponse?.results?.size)
            assert(vm.characterListUIState.nextPaginatedURL == apiResponse?.paginationInfo?.next)
        }

    @Test
    fun `fetch character detail from the remote source and update the data in the respective ui state, should be successful`() =
        runBlocking {
            val idToFindDetail = 1
            val request = RickMortySingleCharacterAPIRequest(idToFindDetail)
            val result = vm.getCharacterDetailRemote(request)
            assert(!result.isError())
            val apiResponse =
                executeBodyOrReturnNull { result.asSuccess<RickMortySingleCharacterDetailsAPIResponseModel>() }
            assert(apiResponse != null)
            assert(apiResponse?.id == idToFindDetail)
            vm.mapAPIResponseToDetailUIState(result)
            assert(vm.charactersDetailsViewUIState.title == apiResponse?.name)
            assert(vm.charactersDetailsViewUIState.image == apiResponse?.image)
        }
}