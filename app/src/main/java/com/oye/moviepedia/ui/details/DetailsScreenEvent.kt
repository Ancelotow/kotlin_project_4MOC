package com.oye.moviepedia.ui.details

sealed class DetailsScreenEvent {
    data class OnGetMovie(val movieId: Int): DetailsScreenEvent()
}
