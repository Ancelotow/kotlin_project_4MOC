package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.SearchDto

abstract class SearchResult(
    open val id: Int
) {

    companion object {
        fun fromSearchDto(result: SearchDto): SearchResult {
          when(result.media_type) {
                "movie" -> {
                    return MovieSearchResult(
                        id = result.id,
                        title = result.title!!,
                        posterPath = result.poster_path,
                        releaseDate = result.release_date
                    )
                }
                "tv" -> {
                    return TvSearchResult(
                        id = result.id,
                        name = result.name!!,
                        posterPath = result.poster_path,
                        firstAirDate = result.first_air_date
                    )
                }
                "person" -> {
                    return PersonSearchResult(
                        id = result.id,
                        name = result.name!!,
                        profilePath = result.profile_path
                    )
                }
                else -> {
                    throw IllegalArgumentException("Unknown media type: ${result.media_type}")
                }
          }
        }
    }

}