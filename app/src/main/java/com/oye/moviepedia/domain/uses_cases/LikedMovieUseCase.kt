package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LikedMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchNowPlayingMovies(): Flow<LikedMovieState> {
        return flow {
            emit(LikedMovieLoading)
            try {
                emit(LikedMovieSuccess(repository.getNowPlayingMovies()))
            } catch (e: DataException) {
                emit(LikedMovieDataError(e))
            }  catch (e: Exception) {
                emit(LikedMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class LikedMovieState
object LikedMovieLoading: LikedMovieState()
data class LikedMovieSuccess(val movies: List<Movie>): LikedMovieState()
data class LikedMovieDataError(val ex: DataException): LikedMovieState()
data class LikedMovieError(val ex: Exception): LikedMovieState()