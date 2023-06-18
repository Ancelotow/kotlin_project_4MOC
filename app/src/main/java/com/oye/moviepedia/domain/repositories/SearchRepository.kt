package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.domain.entities.SearchResult

interface SearchRepository {

    fun getSearchResult(query: String): List<SearchResult>

}