package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.NewItem
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveMovieInPlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun removeMovie(token: String, listId: Int, newItem: List<NewItem>): Flow<RemoveMovieState> {
        return flow {
            emit(RemoveMovieLoading)
            try {
                emit(RemoveMovieSuccess(repository.removeMovie(token, listId, newItem)))
            } catch (e: DataException) {
                emit(RemoveMovieDataError(e))
            }  catch (e: Exception) {
                emit(RemoveMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class RemoveMovieState
object RemoveMovieLoading: RemoveMovieState()
data class RemoveMovieSuccess(val success: Boolean): RemoveMovieState()
data class RemoveMovieDataError(val ex: DataException): RemoveMovieState()
data class RemoveMovieError(val ex: Exception): RemoveMovieState()