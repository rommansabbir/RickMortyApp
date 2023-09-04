package com.rommansabbir.rickmortyapp.domain

import com.rommansabbir.rickmortyapp.base.appresult.AppResult
import com.rommansabbir.rickmortyapp.base.interactor.UseCase
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortyCharactersListAPIResponse
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepository
import javax.inject.Inject

class GetRickMortyCharacterListUseCase @Inject constructor(private val repository: RickMortyRepository) :
    UseCase<RickMortyCharactersListAPIResponse, RickMortyCharactersListAPIRequest>() {
    override suspend fun run(params: RickMortyCharactersListAPIRequest): AppResult<RickMortyCharactersListAPIResponse> =
        repository.getAllCharacters(params)
}