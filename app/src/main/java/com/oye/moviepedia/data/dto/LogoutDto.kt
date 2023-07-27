package com.oye.moviepedia.data.dto

data class LogoutDto(
    var success: Boolean,
    var status_message: String? = null,
    var status_code: Int? = null,
)
