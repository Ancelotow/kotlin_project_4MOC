package com.oye.moviepedia.data.dto

import com.oye.moviepedia.domain.entities.Movie

data class PlaylistDto (
    val success: Boolean,
    val id: Int,
    val name: String,
    val description: String,
    val number_of_items: Int,
    val movieList: List<Movie>
    )
