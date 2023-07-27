package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: MovieRepository) {

    operator fun invoke(query: String): Flow<SearchState> = flow {
        emit(SearchLoading)
        try {
            val searchResult = withContext(Dispatchers.IO) {
                repository.getSearchResult(query)
            }
            emit(SearchSuccess(searchResult))
        } catch (e: Exception) {
            emit(SearchError(e))
        }
    }

}

sealed class SearchState
object SearchLoading: SearchState()
data class SearchSuccess(val results: List<SearchResult>): SearchState()
data class SearchError(val ex: Exception): SearchState()