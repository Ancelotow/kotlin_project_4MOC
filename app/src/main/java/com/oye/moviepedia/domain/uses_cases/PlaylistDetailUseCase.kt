package com.oye.moviepedia.domain.uses_cases


import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlaylistDetailUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun getList(token: String, listId: Int): Flow<ListDetailState> {
        return flow {
            emit(ListDetailLoading)
            try {
                emit(ListDetailSuccess(repository.getListDetail(token, listId)))
            } catch (e: DataException) {
                emit(ListDetailDataError(e))
            }  catch (e: Exception) {
                emit(ListDetailError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class ListDetailState
object ListDetailLoading: ListDetailState()
data class ListDetailSuccess(val playlistDetail: DetailPlaylistDto): ListDetailState()
data class ListDetailDataError(val ex: DataException): ListDetailState()
data class ListDetailError(val ex: Exception): ListDetailState()