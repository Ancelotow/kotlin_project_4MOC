package com.oye.moviepedia.domain.repositories

interface AuthRepository {
    fun getRequestToken(): String
    fun createSession(request_token: String): String
    /*fun validateTokenWithLogin(requestToken: String, username: String, password: String): MutableLiveData<String?>
    fun getAccountDetails(sessionId: String, accountId: Int): MutableLiveData<AccountDetailDto>*/
}
