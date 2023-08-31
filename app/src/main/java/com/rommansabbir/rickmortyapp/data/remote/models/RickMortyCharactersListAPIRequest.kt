package com.rommansabbir.rickmortyapp.data.remote.models

import androidx.annotation.Keep

@Keep
data class RickMortyCharactersListAPIRequest(val fetchFromLocal: Boolean, val paginatedURL: String?)