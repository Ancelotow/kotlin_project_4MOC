package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PopularMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchPopularMovies(): Flow<PopularMovieState> {
        return flow {
            emit(PopularMovieLoading)
            try {
                emit(PopularMovieSuccess(repository.getPopularMovies()))
            } catch (e: DataException) {
                emit(PopularMovieDataError(e))
            }  catch (e: Exception) {
                emit(PopularMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class PopularMovieState
object PopularMovieLoading: PopularMovieState()
data class PopularMovieSuccess(val movies: List<Movie>): PopularMovieState()
data class PopularMovieDataError(val ex: DataException): PopularMovieState()
data class PopularMovieError(val ex: Exception): PopularMovieState()