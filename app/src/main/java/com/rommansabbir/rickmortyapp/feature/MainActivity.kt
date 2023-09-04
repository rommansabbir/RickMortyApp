package com.rommansabbir.rickmortyapp.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.rommansabbir.networkx.NetworkXProvider
import com.rommansabbir.rickmortyapp.R
import com.rommansabbir.rickmortyapp.base.composeext.FillMaxWidth
import com.rommansabbir.rickmortyapp.base.composeext.InfiniteListHandler
import com.rommansabbir.rickmortyapp.base.composeext.SimpleToolbar
import com.rommansabbir.rickmortyapp.base.failure.Failure
import com.rommansabbir.rickmortyapp.base.failure.manager.FailureManager
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var failureManager: FailureManager

    private val vm by viewModels<MainViewModel>()
    private var isInternetConnected: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this) {
            if (vm.uiState.showDetailsUI) {
                vm.uiState.characterId = -1
                vm.uiState.showDetailsUI = false
            } else {
                this@MainActivity.finish()
            }
        }
        vm.isFirstRun = true
        setContent {
            RickMortyAppTheme {
                /*
                Character list view state should be managed here out of character list view,
                otherwise it will recompose the state again and again,
                will cause network leverage again.
                 */
                val listViewState = rememberLazyListState()
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
                                // update first run to false when data loaded from the remote.
                                vm.isFirstRun = false
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

                    AppEntry(
                        vm.uiState,
                        vm.characterListUIState,
                        vm.charactersDetailsViewUIState,
                        listViewState
                    )

                    if (vm.uiState.failureMessage != null) {
                        val message = vm.uiState.failureMessage ?: nullString()
                        AlertDialog(
                            onDismissRequest = { vm.uiState.failureMessage = null },
                            title = { Text(getString(R.string.error_occurred), fontSize = 20.sp) },
                            text = { Text(message) },
                            confirmButton = {

                            },
                            dismissButton = {
                                TextButton(onClick = { vm.uiState.failureMessage = null }) {
                                    Text(getString(R.string.okay).uppercase())
                                }
                            }
                        )
                    }

                    InfiniteListHandler(listState = listViewState, buffer = 1) {
                        if (!vm.isFirstRun && isInternetConnected) {
                            vm.characterListUIState.loadData = true
                        }
                        if (vm.isFirstRun && isInternetConnected) {
                            vm.characterListUIState.loadData = true
                        }
                    }
                }
            }

        }
        initializeDataLoading()
        NetworkXProvider.isInternetConnectedLiveData.observe(this) {
            // update the status
            this.isInternetConnected = it == true
            /*
            When device is connected to the and before then there was no internet and
            when app first run, start the data loading initialization again.
             */
            if (it && vm.isFirstRun) {
                initializeDataLoading()
            }
        }
    }

    /**
     * Start to load characters list from the local or remote.
     */
    private fun initializeDataLoading() {
        if (vm.characterListUIState.dataList.isEmpty()) {
            vm.characterListUIState.loadData = true
        }
    }

    private fun fetchCharacterDetails(id: Int) {
        mainScope {
            if (isInternetConnected) {
                vm.uiState.failureMessage = null
                vm.uiState.isLoading = true
                vm.charactersDetailsViewUIState.showRootView = false
                val result = vm.getCharacterDetailRemote(
                    RickMortySingleCharacterAPIRequest(
                        id = id
                    )
                )
                if (result.isError()) {
                    vm.uiState.isLoading = false
                    vm.uiState.failureMessage = failureManager.handleFailure(result.asFailure())
                    return@mainScope
                }
                vm.uiState.failureMessage = null
                vm.mapAPIResponseToDetailUIState(result = result)
                vm.charactersDetailsViewUIState.showRootView = true
                vm.uiState.isLoading = false
                vm.uiState.showDetailsUI = true
            } else {
                vm.uiState.failureMessage =
                    failureManager.handleFailure(Failure.HTTP.NetworkConnection)
            }
        }
    }

    private fun fetchDataFromLocal() {
        mainScope {
            vm.uiState.showDetailsUI = false
            vm.uiState.isLoading = true
            val result = vm.getCharactersListFromLocal()
            if (result.isError()) {
                vm.uiState.isLoading = false
                vm.uiState.failureMessage = null
                when {
                    isInternetConnected -> {
                        fetchDataFromRemote()
                    }
                    // when there is no data in cache and apps first run then show internet error.
                    vm.isFirstRun -> {
                        vm.uiState.failureMessage =
                            failureManager.handleFailure(Failure.HTTP.NetworkConnection)
                    }
                }
                return@mainScope
            }
            // update first run to false when data loaded from the cache.
            vm.isFirstRun = false
            vm.mapAPIResponseToUIState(result)
            vm.uiState.isLoading = false
        }
    }

    private fun fetchDataFromRemote() {
        mainScope {
            if (isInternetConnected) {
                vm.uiState.failureMessage = null
                vm.uiState.showDetailsUI = false
                vm.uiState.isLoading = true
                val request = RickMortyCharactersListAPIRequest(
                    false, vm.characterListUIState.nextPaginatedURL
                )
                val result = vm.getCharacterListFromRemote(request = request)
                if (result.isError()) {
                    vm.uiState.failureMessage = failureManager.handleFailure(result.asFailure())
                    vm.uiState.isLoading = false
                    return@mainScope
                }
                vm.uiState.failureMessage = null
                vm.mapAPIResponseToUIState(result)

                vm.uiState.isLoading = false
                //
                vm.cacheCharactersListToLocal(
                    CacheCharactersListRequestModel(
                        vm.characterListUIState.nextPaginatedURL,
                        vm.characterListUIState.dataList
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun AppEntry(
        uiState: MainUIState,
        charactersListUIState: CharactersViewUIState,
        charactersDetailsViewUIState: CharactersDetailsViewUIState,
        listViewState: LazyListState
    ) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize(), topBar = {
            SimpleToolbar(
                title = if (uiState.showDetailsUI) "Character Details" else "Rick and Morty",
                showBackButton = uiState.showDetailsUI,
                showLoading = uiState.isLoading
            ) {
                uiState.characterId = -1
                uiState.showDetailsUI = false
            }
        }) { values ->
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
                    ShowCharactersListUI(uiState = charactersListUIState, state = listViewState) {
                        vm.uiState.failureMessage = null
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

