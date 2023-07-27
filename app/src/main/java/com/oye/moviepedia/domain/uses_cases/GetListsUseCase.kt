package com.oye.moviepedia.domain.uses_cases

import android.util.Log
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Playlist
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetListsUseCase @Inject constructor(private val repository: PlaylistRepository) {

    operator fun invoke(token: String, accountId: String): Flow<GetListsState> {
        Log.d("log", "dans use case")

        return flow {
            emit(GetListsLoading)
            try {
                emit(GetListsSuccess(repository.getLists(token, accountId)))
            } catch (e: DataException) {
                emit(GetListsDataError(e))
            }  catch (e: Exception) {
                emit(GetListsError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class GetListsState
object GetListsLoading: GetListsState()
data class GetListsSuccess(val lists: List<Playlist>): GetListsState()
data class GetListsDataError(val ex: DataException): GetListsState()
data class GetListsError(val ex: Exception): GetListsState()