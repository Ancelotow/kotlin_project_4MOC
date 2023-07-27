package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.domain.entities.Movie

interface DynamicLinkDataSource {
    suspend fun createDynamicLinkForMovie(movie: MovieDto): String;
}