package com.oye.moviepedia.data.repositories

import com.oye.moviepedia.data.data_sources.DynamicLinkDataSource
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.repositories.DynamicLinkRepository
import javax.inject.Inject

class FirebaseDynamicLinkRepository @Inject constructor(
    private val dataSource: DynamicLinkDataSource
) : DynamicLinkRepository {

    override suspend fun createDynamicLinkForMovie(movie: MovieDetails): String {
        return dataSource.createDynamicLinkForMovie(MovieDto.fromMovieDetail(movie))
    }

}