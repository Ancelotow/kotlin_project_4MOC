package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.GenreDto

interface GenreDataSource {

    fun fetchMovieGenres(): List<GenreDto>

    fun fetchTvGenres(): List<GenreDto>

}