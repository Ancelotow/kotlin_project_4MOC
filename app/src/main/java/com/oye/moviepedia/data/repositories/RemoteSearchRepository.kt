package com.oye.moviepedia.data.repositories

import com.oye.moviepedia.data.data_sources.GenreDataSource
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.repositories.SearchRepository
import javax.inject.Inject

class RemoteSearchRepository @Inject constructor(
    private val dataSource: MovieDataSource
) : SearchRepository {

    override fun getSearchResult(query: String): List<SearchResult> {
        val resultDto = dataSource.fetchSearchResult(
            query = query,
        )
        return resultDto.map { SearchResult.fromSearchDto(it) }
    }

}