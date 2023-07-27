package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.CastDto
import com.oye.moviepedia.data.dto.MovieDto
import java.time.LocalDate

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val originalTitle: String,
    val description: String,
    val noteAverage: Float,
    val noteCount: Int,
    val releaseDate: LocalDate,
    val isAdult: Boolean,
    val genres: List<Genre>?,
    val runtime: Int?,
    val cast: List<Cast>,
    val trailerKey: String?,
    val director: String
)




