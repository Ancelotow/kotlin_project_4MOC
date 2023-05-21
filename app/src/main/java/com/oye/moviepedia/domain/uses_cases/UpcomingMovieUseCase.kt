package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpcomingMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchUpcomingPlayingMovies(): Flow<UpcomingMovieState> {
        return flow {
            emit(UpcomingMovieLoading)
            try {
                emit(UpcomingMovieSuccess(repository.getUpcomingMovies()))
            } catch (e: DataException) {
                emit(UpcomingMovieDataError(e))
            }  catch (e: Exception) {
                emit(UpcomingMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class UpcomingMovieState
object UpcomingMovieLoading: UpcomingMovieState()
data class UpcomingMovieSuccess(val movies: List<Movie>): UpcomingMovieState()
data class UpcomingMovieDataError(val ex: DataException): UpcomingMovieState()
data class UpcomingMovieError(val ex: Exception): UpcomingMovieState()