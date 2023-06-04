package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchNewMovies(): Flow<NewMovieState> {
        return flow {
            emit(NewMovieLoading)
            try {
                emit(NewMovieSuccess(repository.getNewMovies()))
            } catch (e: DataException) {
                emit(NewMovieDataError(e))
            }  catch (e: Exception) {
                emit(NewMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class NewMovieState
object NewMovieLoading: NewMovieState()
data class NewMovieSuccess(val movies: List<Movie>): NewMovieState()
data class NewMovieDataError(val ex: DataException): NewMovieState()
data class NewMovieError(val ex: Exception): NewMovieState()