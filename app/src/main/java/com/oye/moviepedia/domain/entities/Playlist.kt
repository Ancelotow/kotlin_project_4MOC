package com.oye.moviepedia.domain.entities

import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.PlaylistDto

data class Playlist (
    val id: Int,
    val name: String,
    val description: String,
    val number_of_items: Int,
    val movieList: List<Movie>?,
) {
    companion object {
        fun fromPlaylistDto(playlist: PlaylistDto): Playlist {
            return Playlist(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                number_of_items = playlist.number_of_items,
                movieList = playlist.movieList
            )
        }
    }
}