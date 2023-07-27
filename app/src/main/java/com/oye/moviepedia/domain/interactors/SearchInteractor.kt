package com.oye.moviepedia.domain.interactors

import com.oye.moviepedia.domain.uses_cases.SearchUseCase
import javax.inject.Inject

class SearchInteractor @Inject constructor(
    val searchUseCase: SearchUseCase,
)