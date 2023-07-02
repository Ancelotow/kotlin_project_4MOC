package com.oye.moviepedia.domain.entities

import java.time.LocalDate

data class MovieSearchResult(
    override val id: Int,
    val title: String,
    val posterPath: String?,
    val releaseDate: LocalDate?,
    val genres: List<Genre>
) : SearchResult(id)