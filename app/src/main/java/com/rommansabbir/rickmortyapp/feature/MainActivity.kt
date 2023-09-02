package com.rommansabbir.rickmortyapp.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rommansabbir.rickmortyapp.base.composeext.FillMaxWidth
import com.rommansabbir.rickmortyapp.base.composeext.InfiniteListHandler
import com.rommansabbir.rickmortyapp.base.composeext.SimpleToolbar
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterAPIRequest
import com.rommansabbir.rickmortyapp.feature.characterdetailview.CharacterDetailsUI
import com.rommansabbir.rickmortyapp.feature.characterdetailview.CharactersDetailsViewUIState
import com.rommansabbir.rickmortyapp.feature.charactersview.CharacterView
import com.rommansabbir.rickmortyapp.feature.charactersview.CharactersViewUIState
import com.rommansabbir.rickmortyapp.ui.theme.RickMortyAppTheme
import com.rommansabbir.rickmortyapp.utils.extensions.executeBodyOrReturnNull
import com.rommansabbir.rickmortyapp.utils.extensions.mainScope
import com.rommansabbir.rickmortyapp.utils.extensions.nullString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.isFirstRun = true
        setContent {
            RickMortyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    if (vm.characterListUIState.loadData) {
                        vm.characterListUIState.loadData = false
                        LaunchedEffect(key1 = "", block = {
                            if (vm.isFirstRun) {
                                fetchDataFromLocal()
                            } else {
                                fetchDataFromRemote()
                            }
                        })
                    }


                    if (vm.charactersDetailsViewUIState.loadData) {
                        vm.charactersDetailsViewUIState.loadData = false
                        if (vm.uiState.characterId != -1)
                            LaunchedEffect(key1 = "", block = {
                                fetchCharacterDetails(vm.uiState.characterId)
                            })
                    }

                    AppEntry(vm.uiState, vm.characterListUIState, vm.charactersDetailsViewUIState)
                }
            }

        }
        if (vm.characterListUIState.dataList.isEmpty()) {
            vm.characterListUIState.loadData = true
        }
    }

    private fun fetchCharacterDetails(id: Int) {
        mainScope {
            vm.uiState.isLoading = true
            vm.charactersDetailsViewUIState.showRootView = false
            val result = vm.getCharacterDetailRemote(
                RickMortySingleCharacterAPIRequest(
                    id = id
                )
            )
            if (result.isError()) {
                vm.uiState.isLoading = false
                return@mainScope
            }
            vm.mapAPIResponseToDetailUIState(result = result)
            vm.charactersDetailsViewUIState.showRootView = true
            vm.uiState.isLoading = false
            vm.uiState.showDetailsUI = true
        }
    }

    private fun fetchDataFromLocal() {
        mainScope {
            vm.uiState.showDetailsUI = false
            vm.uiState.isLoading = true
            val result = vm.getCharactersListFromLocal()
            if (result.isError()) {
                vm.uiState.isLoading = false
                fetchDataFromRemote()
                return@mainScope
            }
            vm.mapAPIResponseToUIState(result)
            vm.uiState.isLoading = false
        }
    }

    private fun fetchDataFromRemote() {
        mainScope {
            vm.uiState.showDetailsUI = false
            vm.uiState.isLoading = true
            vm.isFirstRun = false
            val request = RickMortyCharactersListAPIRequest(
                false, vm.characterListUIState.nextPaginatedURL
            )
            val result = vm.getCharacterListFromRemote(request = request)
            if (result.isError()) {
                vm.uiState.isLoading = false
                return@mainScope
            }
            vm.mapAPIResponseToUIState(result)

            vm.uiState.isLoading = false
            //
            vm.cacheCharactersListToLocal(
                CacheCharactersListRequestModel(
                    vm.characterListUIState.nextPaginatedURL, vm.characterListUIState.dataList
                )
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun AppEntry(
        uiState: MainUIState,
        charactersListUIState: CharactersViewUIState,
        charactersDetailsViewUIState: CharactersDetailsViewUIState
    ) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize(), topBar = {
            SimpleToolbar(
                title = "Rick and Morty",
                showBackButton = uiState.showDetailsUI,
                showLoading = uiState.isLoading
            ) {
                uiState.characterId = -1
                uiState.showDetailsUI = false
            }
        }) { values ->
            val state = rememberLazyListState()
            if (uiState.showDetailsUI) {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    if (charactersDetailsViewUIState.showRootView) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(values)
                        ) {
                            ShowCharacterDetailUI(uiState = charactersDetailsViewUIState)
                        }
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(values)
                ) {
                    ShowCharactersListUI(uiState = charactersListUIState, state = state) {
                        uiState.characterId = it
                        uiState.showDetailsUI = true
                        charactersDetailsViewUIState.loadData = true
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowCharacterDetailUI(uiState: CharactersDetailsViewUIState) {
        Column(
            FillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CharacterDetailsUI(
                name = uiState.title,
                imageURL = uiState.image,
                info = uiState.informationList,
                totalEpisodes = uiState.totalEpisodes
            )
        }
    }


    @Composable
    private fun ShowCharactersListUI(
        uiState: CharactersViewUIState,
        state: LazyListState,
        onItemDetail: (id: Int) -> Unit
    ) {
        InfiniteListHandler(listState = state) {
            uiState.loadData = true
        }
        LazyColumn(
            state = state
        ) {
            items(uiState.dataList.size, key = { uiState.dataList[it].id ?: 0 }) { index ->
                val model = executeBodyOrReturnNull {
                    return@executeBodyOrReturnNull uiState.dataList[index]
                } ?: kotlin.run {
                    null
                }
                model?.let {
                    CharacterView(
                        modifier = Modifier,
                        id = it.id ?: -1,
                        name = it.name ?: nullString(),
                        imageUrl = it.image ?: "",
                        species = it.species ?: nullString(),
                        isAlive = it.status?.contains("alive", true) == true,
                        gender = it.gender ?: nullString(),
                        totalEpisodes = it.episode.size,
                        onItemDetail = onItemDetail
                    )
                }
            }
        }
    }
}

