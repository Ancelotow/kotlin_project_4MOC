package com.oye.moviepedia.data.dto

data class ListMovieResultDto(
    val page: Int,
    val results: List<MovieDto>
)