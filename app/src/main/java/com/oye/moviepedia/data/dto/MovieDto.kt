package com.oye.moviepedia.data.dto

import com.oye.moviepedia.domain.entities.Cast
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
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
    val genres: List<GenreDto>?,
    val runtime: Int?,
    val video: Boolean,
    val vote_average:Float,
    val vote_count: Int,
    val release_date: LocalDate
) {

    companion object {
        fun fromMovieDetail(movie: MovieDetails): MovieDto {
            return MovieDto(
                id = movie.id,
                title = movie.title,
                original_title = movie.originalTitle,
                overview = movie.description,
                vote_average = movie.noteAverage,
                vote_count = movie.noteCount,
                adult = movie.isAdult,
                release_date = movie.releaseDate,
                poster_path = movie.posterUrl,
                genres = null,
                runtime = null,
                backdrop_path = "",
                original_language = "",
                popularity = 0f,
                video = false
            )
        }
    }


    fun toMovie(director: String): Movie {
        return Movie(
            id = id,
            title = title,
            originalTitle = original_title,
            description = overview,
            noteAverage = vote_average,
            noteCount = vote_count,
            isAdult = adult,
            releaseDate = release_date,
            posterUrl = "https://www.themoviedb.org/t/p/original/${poster_path}",
            director = director
        )
    }

    fun toMovieDetail(cast: List<CastDto>, director: String, trailerKey: String?): MovieDetails {
        return MovieDetails(
            id = id,
            title = title,
            originalTitle = original_title,
            description = overview,
            noteAverage = vote_average,
            noteCount = vote_count,
            isAdult = adult,
            releaseDate = release_date,
            posterUrl = "https://www.themoviedb.org/t/p/original/${poster_path}",
            genres = genres?.map { it.toGenre() },
            runtime = runtime,
            cast = cast.map {
                Cast(
                    it.cast_id,
                    it.character,
                    it.original_name,
                    "https://www.themoviedb.org/t/p/original/${it.profile_path}"
                )
            },
            trailerKey = trailerKey,
            director = director
        )
    }
}