package com.oye.moviepedia.data.repositories

import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.domain.repositories.AuthRepository
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(
    private val dataSource: AuthDataSource
) : AuthRepository {

    override fun getRequestToken(): String {
        val tokenDto = dataSource.getRequestToken()
        val token = tokenDto.request_token
        return token;
    }

    override fun getAccountId(requestToken: String): AuthDto {
        val authDto = dataSource.getAccountId(requestToken)
        return authDto
    }

}
