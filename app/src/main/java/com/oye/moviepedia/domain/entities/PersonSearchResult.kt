package com.oye.moviepedia.domain.entities

data class PersonSearchResult(
    override val id: Int,
    val name: String,
    val mainJob: String,
    val profilePath: String?,
) : SearchResult(id)

