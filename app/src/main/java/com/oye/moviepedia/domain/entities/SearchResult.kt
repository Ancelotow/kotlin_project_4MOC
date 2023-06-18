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
                        posterPath = "${baseUriImage}/${result.poster_path}",
                        releaseDate = result.release_date
                    )
                }

                "tv" -> {
                    return TvSearchResult(
                        id = result.id,
                        name = result.name!!,
                        posterPath = "${baseUriImage}/${result.poster_path}",
                        firstAirDate = result.first_air_date
                    )
                }

                "person" -> {
                    return PersonSearchResult(
                        id = result.id,
                        name = result.name!!,
                        profilePath = "${baseUriImage}/${result.profile_path}",
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