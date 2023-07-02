package com.oye.moviepedia.data.dto

import com.oye.moviepedia.domain.entities.Genre

data class GenreDto(
    val id: Int,
    val name: String
) {
    fun toGenre() = Genre(id, name)
}