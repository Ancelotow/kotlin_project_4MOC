package com.oye.moviepedia.domain.interactors

import com.oye.moviepedia.domain.uses_cases.NewMovieUseCase
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieUseCase
import com.oye.moviepedia.domain.uses_cases.PopularMovieUseCase
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieUseCase
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    val newMovieUseCase: NewMovieUseCase,
    val nowPlayingMovieUseCase: NowPlayingMovieUseCase,
    val popularMovieUseCase: PopularMovieUseCase,
    val upcomingMovieUseCase: UpcomingMovieUseCase,
)