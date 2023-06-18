package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.SearchDto

abstract class SearchResult(
    open val id: Int
) {

    companion object {
        fun fromSearchDto(result: SearchDto): SearchResult {
            val baseUriImage = "https://www.themoviedb.org/t/p/w220_and_h330_face"
            when (result.media_type) {
                "movie" -> {
                    return MovieSearchResult(
                        id = result.id,
                        title = result.title!!,
                        posterPath = if (result.poster_path != null) "${baseUriImage}/${result.poster_path}" else null,
                        releaseDate = result.release_date
                    )
                }

                "tv" -> {
                    return TvSearchResult(
                        id = result.id,
                        name = result.name!!,
                        posterPath = if (result.poster_path != null) "${baseUriImage}/${result.poster_path}" else null ,
                        firstAirDate = result.first_air_date
                    )
                }

                "person" -> {
                    return PersonSearchResult(
                        id = result.id,
                        name = result.name!!,
                        profilePath = if (result.profile_path != null) "${baseUriImage}/${result.profile_path}" else null,
                        mainJob = result.known_for_department ?: "Unknown"
                    )
                }

                else -> {
                    throw IllegalArgumentException("Unknown media type: ${result.media_type}")
                }
            }
        }
    }

}