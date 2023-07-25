package com.oye.moviepedia.domain.uses_cases

import android.util.Log
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveMovieInPlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun removeMovie(token: String, listId: Int, item: ListItems): Flow<RemoveMovieState> {
        return flow {
            emit(RemoveMovieLoading)
            try {
                Log.d("log", "dans use case")
                emit(RemoveMovieSuccess(repository.removeMovie(token, listId, item)))
            } catch (e: DataException) {
                Log.d("log", "dans use case data execption : $e")
                emit(RemoveMovieDataError(e))
            }  catch (e: Exception) {
                Log.d("log", "dans use case execption : $e")

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