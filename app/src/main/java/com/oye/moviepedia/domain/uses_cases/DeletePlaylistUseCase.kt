package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun deletePlaylist(token: String, listId: Int): Flow<DeletePlaylistState> {
        return flow {
            emit(DeletePlaylistLoading)
            try {
                emit(DeletePlaylistSuccess(repository.deletePlaylist(token, listId)))
            } catch (e: DataException) {
                emit(DeletePlaylistDataError(e))
            }  catch (e: Exception) {
                emit(DeletePlaylistError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class DeletePlaylistState
object DeletePlaylistLoading: DeletePlaylistState()
data class DeletePlaylistSuccess(val success: Boolean): DeletePlaylistState()
data class DeletePlaylistDataError(val ex: DataException): DeletePlaylistState()
data class DeletePlaylistError(val ex: Exception): DeletePlaylistState()