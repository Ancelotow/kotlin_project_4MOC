package com.oye.moviepedia.domain.interactors

import com.oye.moviepedia.domain.uses_cases.CreateListUseCase
import com.oye.moviepedia.domain.uses_cases.GetListsUseCase
import javax.inject.Inject

data class ProfileInteractor @Inject constructor(
    val useCaseCreateList: CreateListUseCase,
    val useCaseGetLists: GetListsUseCase,
)
