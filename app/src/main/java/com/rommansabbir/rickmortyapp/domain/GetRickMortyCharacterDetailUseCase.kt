package com.rommansabbir.rickmortyapp.domain

import com.rommansabbir.rickmortyapp.base.appresult.AppResult
import com.rommansabbir.rickmortyapp.base.interactor.UseCase
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterAPIRequest
import com.rommansabbir.rickmortyapp.data.remote.models.RickMortySingleCharacterDetailsAPIResponseModel
import com.rommansabbir.rickmortyapp.data.remote.repository.RickMortyRepository
import javax.inject.Inject

class GetRickMortyCharacterDetailUseCase @Inject constructor(private val repository: RickMortyRepository) :
    UseCase<RickMortySingleCharacterDetailsAPIResponseModel, RickMortySingleCharacterAPIRequest>() {
    override suspend fun run(params: RickMortySingleCharacterAPIRequest): AppResult<RickMortySingleCharacterDetailsAPIResponseModel> =
        repository.getSingleCharacterDetails(params)
}