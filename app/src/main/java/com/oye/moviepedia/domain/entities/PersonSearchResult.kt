package com.oye.moviepedia.domain.entities

data class PersonSearchResult(
    override val id: Int,
    val name: String,
    val profilePath: String?,
) : SearchResult(id)

