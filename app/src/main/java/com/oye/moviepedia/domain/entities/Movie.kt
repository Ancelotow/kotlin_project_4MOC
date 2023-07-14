package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.MovieDto
import java.time.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val originalTitle: String,
    val description: String,
    val noteAverage: Float,
    val noteCount: Int,
    val releaseDate: LocalDate,
    val isAdult: Boolean,
    val director: String
)

