package com.oye.moviepedia.domain.entities

import java.time.LocalDate

data class MovieSearchResult(
    override val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: LocalDate?,
) : SearchResult(id)