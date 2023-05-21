package com.oye.moviepedia.data.dto

data class CastDto(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val know_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Float,
    val profile_path: String,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int
)