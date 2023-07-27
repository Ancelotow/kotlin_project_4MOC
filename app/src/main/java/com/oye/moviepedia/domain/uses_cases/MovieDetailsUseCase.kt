package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieDetailsUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(id: Int): Flow<MovieDetailsState> {
        return flow {
            emit(MovieDetailsLoading)
            try {
                repository.getMovieDetails(id)?.let {
                    emit(MovieDetailsSuccess(it))
                } ?: run {
                    emit(MovieDetailsError(Exception("No movie found")))
                }
            } catch (e: DataException) {
                emit(MovieDetailsDataError(e))
            }  catch (e: Exception) {
                emit(MovieDetailsError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class MovieDetailsState
object MovieDetailsLoading: MovieDetailsState()
data class MovieDetailsSuccess(val movie: MovieDetails): MovieDetailsState()
data class MovieDetailsDataError(val ex: DataException): MovieDetailsState()
data class MovieDetailsError(val ex: Exception): MovieDetailsState()