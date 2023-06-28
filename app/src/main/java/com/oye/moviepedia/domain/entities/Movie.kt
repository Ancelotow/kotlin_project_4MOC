package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.Genre
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
    val genres: List<Genre>?,
    val runtime: Int?,
    val director: String
) {
    companion object {
        fun fromMovieDto(movie: MovieDto, director: String): Movie {
            return Movie(
                id = movie.id,
                title = movie.title,
                originalTitle = movie.original_title,
                description = movie.overview,
                noteAverage = movie.vote_average,
                noteCount = movie.vote_count,
                isAdult = movie.adult,
                releaseDate = movie.release_date,
                posterUrl = "https://www.themoviedb.org/t/p/original/${movie.poster_path}",
                genres = movie.genres,
                runtime = movie.runtime,
                director = director
            )

        }
    }
}

