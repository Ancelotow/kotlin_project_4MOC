package com.oye.moviepedia.data.dto

data class TokenDto(
    val success: Boolean,
    val expires_at: String,
    val request_token: String,
)