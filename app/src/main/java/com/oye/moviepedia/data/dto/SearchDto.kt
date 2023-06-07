package com.oye.moviepedia.data.dto

import java.time.LocalDate

data class SearchDto(
    val id: Int,
    val name: String?,
    val profile_path: String?,
    val title: String?,
    val poster_path: String?,
    val release_date: LocalDate?,
    val first_air_date: LocalDate?,
    val media_type: String
)
