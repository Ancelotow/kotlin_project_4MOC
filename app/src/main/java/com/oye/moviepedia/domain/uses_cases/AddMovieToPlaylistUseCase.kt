package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddMovieToPlaylistUseCase @Inject constructor(private val repository: PlaylistRepository) {

    suspend fun addMovie(token: String, listId: Int, newItem: ListItems): Flow<AddMovieState> {
        return flow {
            emit(AddMovieLoading)
            try {
                emit(AddMovieSuccess(repository.addMovie(token, listId, newItem)))
            } catch (e: DataException) {
                emit(AddMovieDataError(e))
            }  catch (e: Exception) {
                emit(AddMovieError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

sealed class AddMovieState
object AddMovieLoading: AddMovieState()
data class AddMovieSuccess(val success: Boolean): AddMovieState()
data class AddMovieDataError(val ex: DataException): AddMovieState()
data class AddMovieError(val ex: Exception): AddMovieState()