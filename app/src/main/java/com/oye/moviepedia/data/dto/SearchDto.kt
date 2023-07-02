package com.oye.moviepedia.data.dto

import com.oye.moviepedia.domain.entities.Genre
import com.oye.moviepedia.domain.entities.MovieSearchResult
import com.oye.moviepedia.domain.entities.PersonSearchResult
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.entities.TvSearchResult
import java.time.LocalDate

data class SearchDto(
    val id: Int,
    val name: String?,
    val profile_path: String?,
    val known_for_department: String?,
    val title: String?,
    val poster_path: String?,
    val release_date: LocalDate?,
    val first_air_date: LocalDate?,
    val media_type: String,
    val genre_ids: List<Int>?
) {
    fun toSearchResult(tvGenreDto: List<Genre>, movieGenreDto: List<Genre>): SearchResult {
        val baseUriImage = "https://www.themoviedb.org/t/p/w220_and_h330_face"
        when (media_type) {
            "movie" -> {
                return MovieSearchResult(
                    id = id,
                    title = title!!,
                    posterPath = if (poster_path != null) "${baseUriImage}/${poster_path}" else null,
                    releaseDate = release_date,
                    genres = if (genre_ids != null) movieGenreDto.filter { genre_ids.contains(it.id) }
                        .map { it } else mutableListOf()
                )
            }

            "tv" -> {
                return TvSearchResult(
                    id = id,
                    name = name!!,
                    posterPath = if (poster_path != null) "${baseUriImage}/${poster_path}" else null,
                    firstAirDate = first_air_date,
                    genres = if (genre_ids != null) tvGenreDto.filter { genre_ids.contains(it.id) }
                        .map { it } else mutableListOf()
                )
            }

            "person" -> {
                return PersonSearchResult(
                    id = id,
                    name = name!!,
                    profilePath = if (profile_path != null) "${baseUriImage}/${profile_path}" else null,
                    mainJob = known_for_department ?: "Unknown"
                )
            }

            else -> {
                throw IllegalArgumentException("Unknown media type: $media_type")
            }
        }
    }
}
