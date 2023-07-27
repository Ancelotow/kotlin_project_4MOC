package com.oye.moviepedia.domain.interactors

import com.oye.moviepedia.domain.uses_cases.AuthUseCase
import javax.inject.Inject

data class LoginInteractor @Inject constructor(
    val useCaseAuth: AuthUseCase,
    )
