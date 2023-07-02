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
) {
    companion object {
        fun fromMovieDto(movie: MovieDto, cast: List<CastDto>, director: String, trailerKey: String?): MovieDetails {
            return MovieDetails(
                id = movie.id,
                title = movie.title,
                originalTitle = movie.original_title,
                description = movie.overview,
                noteAverage = movie.vote_average,
                noteCount = movie.vote_count,
                isAdult = movie.adult,
                releaseDate = movie.release_date,
                posterUrl = "https://www.themoviedb.org/t/p/original/${movie.poster_path}",
                genres = movie.genres?.map { it.toGenre() },
                runtime = movie.runtime,
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
}

data class Cast(
    val id: Int,
    val character: String,
    val originalName: String,
    val picturePath: String
)


