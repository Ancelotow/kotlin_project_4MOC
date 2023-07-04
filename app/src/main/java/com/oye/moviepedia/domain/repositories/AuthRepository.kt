package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.dto.SessionDto

interface AuthRepository {
    fun getRequestToken(): String
    fun createSession(request_token: String): SessionDto
    /*fun validateTokenWithLogin(requestToken: String, username: String, password: String): MutableLiveData<String?>
    fun getAccountDetails(sessionId: String, accountId: Int): MutableLiveData<AccountDetailDto>*/
}
