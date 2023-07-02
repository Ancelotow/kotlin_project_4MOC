package com.oye.moviepedia.data.dto

import java.time.LocalDate

data class MovieDto(
    val adult: Boolean,
    val backdrop_path: String,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val title: String,
    val genres: List<Genre>?,
    val runtime: Int?,
    val video: Boolean,
    val vote_average:Float,
    val vote_count: Int,
    val release_date: LocalDate
)

data class Genre(
    val id: Int?,
    val name: String?
)