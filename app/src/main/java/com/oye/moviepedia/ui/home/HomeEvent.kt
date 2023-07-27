package com.oye.moviepedia.ui.home

sealed class HomeEvent {
    object OnUpcomingMovies : HomeEvent()
    object OnPopularMovies : HomeEvent()
    object OnNowPlayingMovies : HomeEvent()
    object OnNewMovies : HomeEvent()
}