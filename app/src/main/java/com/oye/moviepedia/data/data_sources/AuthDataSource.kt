package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.dto.TokenDto

interface AuthDataSource {

    fun getRequestToken(): TokenDto
    fun getAccountId(requestToken: String): AuthDto
}