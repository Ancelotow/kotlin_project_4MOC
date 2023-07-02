package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.entities.SearchResult

interface MovieRepository {

    fun getUpcomingMovies(): List<Movie>

    fun getNewMovies(): List<Movie>

    fun getNowPlayingMovies(): List<Movie>

    fun getPopularMovies(): List<Movie>

    fun getMovieDetails(id: Int): MovieDetails?

    fun getSearchResult(query: String): List<SearchResult>

}