package com.oye.moviepedia.data.dto

data class ListSearchResultDto(
    val page: Int,
    val results: List<SearchDto>,
)