package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.SessionDto
import com.oye.moviepedia.data.dto.TokenDto

interface AuthDataSource {

    fun getRequestToken(): TokenDto
    fun createSession(requestToken: String): SessionDto
}