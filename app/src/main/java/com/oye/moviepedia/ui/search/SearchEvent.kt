package com.oye.moviepedia.ui.search

sealed class SearchEvent {
    data class OnSearchMovies(val search: String) : SearchEvent()
}