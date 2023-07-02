package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend fun fetchSearchResult(query: String): Flow<SearchState> {
        return flow {
            emit(SearchLoading)
            try {
                emit(SearchSuccess(repository.getSearchResult(query)))
            } catch (e: DataException) {
                emit(SearchDataError(e))
            }  catch (e: Exception) {
                emit(SearchError(e))
            }
        }.flowOn(Dispatchers.IO)
    }

}

sealed class SearchState
object SearchLoading: SearchState()
data class SearchSuccess(val results: List<SearchResult>): SearchState()
data class SearchDataError(val ex: DataException): SearchState()
data class SearchError(val ex: Exception): SearchState()