package com.oye.moviepedia.ui.details

import com.oye.moviepedia.domain.entities.MovieDetails

sealed class DetailsScreenEvent {
    data class OnGetMovie(val movieId: Int): DetailsScreenEvent()
    data class OnGetDynamicLink(val movie: MovieDetails): DetailsScreenEvent()
}
