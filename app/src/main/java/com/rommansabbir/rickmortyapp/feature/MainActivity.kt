package com.rommansabbir.rickmortyapp.feature

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rommansabbir.rickmortyapp.base.composeext.InfiniteListHandler
import com.rommansabbir.rickmortyapp.base.composeext.SimpleToolbar
import com.rommansabbir.rickmortyapp.data.local.models.CacheCharactersListRequestModel
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
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
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    if (vm.characterListUIState.loadData) {
                        vm.characterListUIState.loadData = false
                        LaunchedEffect(key1 = "", block = {
                            if (vm.isFirstRun) {
                                fetchDataFromLocal(context)
                            } else {
                                fetchDataFromRemote(context)
                            }
                        })
                        //
                    }

                    AppEntry(vm.uiState, vm.characterListUIState)
                }
            }

        }
        if (vm.characterListUIState.dataList.isEmpty()) {
            vm.characterListUIState.loadData = true
        }
    }

    private fun fetchDataFromLocal(context: Context) {
        mainScope {
            vm.uiState.isLoading = true
            val result = vm.getCharactersListFromLocal()
            if (result.isError()) {
                vm.uiState.isLoading = false
                fetchDataFromRemote(context)
                return@mainScope
            }
            vm.mapAPIResponseToUIState(result)
            vm.uiState.isLoading = false
        }
    }

    private fun fetchDataFromRemote(context: Context) {
        mainScope {
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

    @Composable
    private fun AppEntry(uiState: MainUIState, charactersListUIState: CharactersViewUIState) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize(), topBar = {
            SimpleToolbar(
                title = "Rick and Morty", showBackButton = true, showLoading = uiState.isLoading
            )
        }) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                ShowCharactersListUI(uiState = charactersListUIState)
            }
        }
    }


    @Composable
    private fun ShowCharactersListUI(uiState: CharactersViewUIState) {
        val state = rememberLazyListState()
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
                        gender = it.gender ?: nullString()
                    ) {

                    }
                }
            }
        }
    }
}

