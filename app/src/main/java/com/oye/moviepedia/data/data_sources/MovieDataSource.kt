package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.GenreDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.SearchDto
import com.oye.moviepedia.data.dto.MovieTrailerDto
import java.time.LocalDate

interface MovieDataSource {

    fun fetchMovies(primaryDateRelease: LocalDate?, sortBy: String?, includeAdult: Boolean = false): List<MovieDto>

    fun fetchNowPlayingMovies(): List<MovieDto>

    fun fetchCreditsMovies(id: Int): CreditsResultDto

    fun fetchPopularMovies(): List<MovieDto>

    fun fetchSearchResult(query: String): List<SearchDto>

    fun getMovieDetails(id: Int): MovieDto?

    fun getMovieTrailers(id: Int): MovieTrailerDto?

    fun fetchMovieGenres(): List<GenreDto>

    fun fetchTvGenres(): List<GenreDto>

}