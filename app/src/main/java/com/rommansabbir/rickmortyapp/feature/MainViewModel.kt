package com.rommansabbir.rickmortyapp.feature

import androidx.lifecycle.ViewModel
import com.rommansabbir.rickmortyapp.base.apiresult.APIResult
import com.rommansabbir.rickmortyapp.base.interactor.UseCase
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.domain.CacheCharactersListToLocalUseCase
import com.rommansabbir.rickmortyapp.domain.GetCharactersListFromLocalUseCase
import com.rommansabbir.rickmortyapp.domain.GetRickMortyCharacterListUseCase
import com.rommansabbir.rickmortyapp.feature.charactersview.CharactersViewUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterListUseCase: GetRickMortyCharacterListUseCase,
    private val cacheCharactersListToLocalUseCase: CacheCharactersListToLocalUseCase,
    private val getCharactersListFromLocalUseCase: GetCharactersListFromLocalUseCase
) : ViewModel() {
    /**
     * Identify if is this the first run of the associated Activity.
     * Based on this client will decide to load characters from
     * the local cache or the remote source (API).
     */
    var isFirstRun: Boolean = false

    /**
     * Lazy instance of [CharactersViewUIState]. ViewModel holds the latest state to represent the
     * characters list UI.
     */
    val characterListUIState by lazy { CharactersViewUIState() }

    /**
     * Lazy instance of [MainUIState]. ViewModel holds the latest state to represent the
     * activity UI.
     */
    val uiState by lazy { MainUIState() }

    /**
     * Get new characters list from the remote source (API). Either success or error.
     *
     * @param request Request model.
     *
     * @return [APIResult]<[RickMortyCharactersListAPIResponse]>
     */
    suspend fun getCharacterListFromRemote(request: RickMortyCharactersListAPIRequest): APIResult<RickMortyCharactersListAPIResponse> =
        characterListUseCase(request)

    /**
     * Get new characters list from the local cache. Either success or error.
     *
     * @return [APIResult]<[RickMortyCharactersListAPIResponse]>
     */
    suspend fun getCharactersListFromLocal(): APIResult<RickMortyCharactersListAPIResponse> =
        getCharactersListFromLocalUseCase(
            UseCase.None()
        )


    /**
     * Cache all characters list to the local including the next paginated url to load data.
     * Either success or error.
     *
     * @param request Request model.
     *
     * @return [APIResult]<[Boolean]>
     */
    suspend fun cacheCharactersListToLocal(request: CacheCharactersListRequestModel) =
        cacheCharactersListToLocalUseCase(request)

    /**
     * Extract the characters list api response model from
     * the [APIResult]<[RickMortyCharactersListAPIResponse]> and update the
     * list into the [characterListUIState].
     *
     * Make sure all works get done under [Dispatchers.Default].
     *
     * @param result [RickMortyCharactersListAPIResponse] wrapped into [APIResult].
     */
    suspend fun mapAPIResponseToUIState(result: APIResult<RickMortyCharactersListAPIResponse>) {
        withContext(Dispatchers.Default) {
            val apiResponse = result.asSuccess<RickMortyCharactersListAPIResponse>()
            characterListUIState.nextPaginatedURL = apiResponse.paginationInfo.next
            val temp = characterListUIState.dataList
            temp.addAll(apiResponse.results)
            characterListUIState.dataList = temp
        }
    }
}