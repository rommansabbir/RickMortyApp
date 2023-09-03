package com.rommansabbir.rickmortyapp.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rommansabbir.rickmortyapp.base.uistate.BaseComposeUIState

class MainUIState : BaseComposeUIState() {
    var showDetailsUI: Boolean by mutableStateOf(false)
    var characterId: Int by mutableIntStateOf(-1)
    var failureMessage : String? by mutableStateOf(null)
}