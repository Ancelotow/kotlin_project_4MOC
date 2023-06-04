package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NowPlayingMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchNowPlayingMovies(): Flow<NowPlayingMovieState> {
        return flow {
            emit(NowPlayingMovieLoading)
            try {
                emit(NowPlayingMovieSuccess(repository.getNowPlayingMovies()))
            } catch (e: DataException) {
                emit(NowPlayingMovieDataError(e))
            }  catch (e: Exception) {
                emit(NowPlayingMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class NowPlayingMovieState
object NowPlayingMovieLoading: NowPlayingMovieState()
data class NowPlayingMovieSuccess(val movies: List<Movie>): NowPlayingMovieState()
data class NowPlayingMovieDataError(val ex: DataException): NowPlayingMovieState()
data class NowPlayingMovieError(val ex: Exception): NowPlayingMovieState()