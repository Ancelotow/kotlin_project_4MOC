package com.oye.moviepedia.data.dto

data class AuthDto(
    var success: Boolean,
    var account_id: String? = null,
    var access_token: String? = null,
)
