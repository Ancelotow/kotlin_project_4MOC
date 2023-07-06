package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.dto.AuthDto

interface AuthRepository {
    fun getRequestToken(): String
    fun getAccountId(requestToken: String): AuthDto

}
