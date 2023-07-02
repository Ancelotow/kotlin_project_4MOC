package com.oye.moviepedia.domain.entities

import java.time.LocalDate

data class TvSearchResult(
    override val id: Int,
    val name: String,
    val posterPath: String?,
    val firstAirDate: LocalDate?,
    val genres: List<Genre>,
) : SearchResult(id)
