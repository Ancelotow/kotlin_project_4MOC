package com.oye.moviepedia.domain.interactors

import com.oye.moviepedia.domain.uses_cases.GetMovieDynamicLinkUseCase
import com.oye.moviepedia.domain.uses_cases.MovieDetailsUseCase
import javax.inject.Inject

data class DetailsInteractor @Inject constructor(
    val movieDetailsUseCase: MovieDetailsUseCase,
    val getMovieDynamicLinkUseCase: GetMovieDynamicLinkUseCase
)