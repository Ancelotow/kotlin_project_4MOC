package com.oye.moviepedia.data.dto

data class AuthDto(
    val success: Boolean,
    val account_id: String,
    val access_token: String,
)
