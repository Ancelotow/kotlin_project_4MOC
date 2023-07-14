package com.oye.moviepedia.domain.uses_cases

import android.util.Log
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CreateListUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun createList(token: String, name: String): Flow<CreateListState> {
        return flow {
            emit(CreateListLoading)
            try {
                emit(CreateListSuccess(repository.createList(token, name)))
            } catch (e: DataException) {
                emit(CreateListDataError(e))
            }  catch (e: Exception) {
                emit(CreateListError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class CreateListState
object CreateListLoading: CreateListState()
data class CreateListSuccess(val status: Boolean): CreateListState()
data class CreateListDataError(val ex: DataException): CreateListState()
data class CreateListError(val ex: Exception): CreateListState()