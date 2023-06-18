package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.domain.entities.Movie

interface MovieRepository {

    fun getUpcomingMovies(): List<Movie>

    fun getNewMovies(): List<Movie>

    fun getNowPlayingMovies(): List<Movie>

    fun getPopularMovies(): List<Movie>

}