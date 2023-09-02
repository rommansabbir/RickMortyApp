package com.rommansabbir.rickmortyapp.feature.characterdetailview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rommansabbir.rickmortyapp.base.uistate.BaseComposeUIState


/**
 * Represent the character detail UI state which extends the Base UI State.
 * All information of the character details are stored here.
 */
class CharactersDetailsViewUIState : BaseComposeUIState() {
    var title: String by mutableStateOf("")
    var image: String by mutableStateOf("")
    var informationList: MutableList<Pair<String, String>> = mutableStateListOf()
    var totalEpisodes: Int by mutableIntStateOf(0)
}