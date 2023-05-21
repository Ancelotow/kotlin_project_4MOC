package com.oye.moviepedia.data.dto

data class CreditsResultDto(
    val id: Int,
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)