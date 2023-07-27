package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.repositories.DynamicLinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDynamicLinkUseCase @Inject constructor(private val repository: DynamicLinkRepository){

    operator fun invoke(movie: MovieDetails): Flow<GetMovieDynamicLinkState> = flow {
        emit(GetMovieDynamicLinkLoading)
        try {
            val shortLink = repository.createDynamicLinkForMovie(movie)
            emit(GetMovieDynamicLinkSuccess(shortLink))
        } catch (e: Exception) {
            emit(GetMovieDynamicLinkError(e))
        }
    }

}

sealed class GetMovieDynamicLinkState
object GetMovieDynamicLinkLoading: GetMovieDynamicLinkState()
data class GetMovieDynamicLinkSuccess(val shortLink: String): GetMovieDynamicLinkState()
data class GetMovieDynamicLinkError(val ex: Exception): GetMovieDynamicLinkState()