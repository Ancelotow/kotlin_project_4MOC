package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.domain.entities.MovieDetails

interface DynamicLinkRepository {

    suspend fun createDynamicLinkForMovie(movie: MovieDetails): String;

}