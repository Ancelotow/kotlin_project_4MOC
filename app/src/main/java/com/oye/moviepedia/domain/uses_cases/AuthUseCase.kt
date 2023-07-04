package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.dto.SessionDto
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: AuthRepository) {

    fun getRequestToken(): Flow<AuthState> {
        return flow {
            emit(AuthLoading)
            try {
                emit(AuthTokenSuccess(repository.getRequestToken()))
            } catch (e: DataException) {
                emit(AuthDataError(e))
            }  catch (e: Exception) {
                emit(AuthError(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun createSession(request_token: String): Flow<AuthState> {
        return flow {
            emit(AuthLoading)
            try {
                emit(AuthSessionSuccess(repository.createSession(request_token)))
            } catch (e: DataException) {
                emit(AuthDataError(e))
            }  catch (e: Exception) {
                emit(AuthError(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /*fun getAccountDetails(sessionId: String, accountId: Int): MutableLiveData<AccountDetailDto> {
        val accountDetailsLiveData = MutableLiveData<AccountDetailDto>()

        apiService.getAccountDetails(sessionId, accountId).enqueue(object : Callback<AccountDetailDto> {
            override fun onFailure(call: Call<AccountDetailDto>, t: Throwable) {
                // Handle request errors
            }

            override fun onResponse(call: Call<AccountDetailDto>, response: Response<AccountDetailDto>) {
                val responseData = response.body()
                accountDetailsLiveData.postValue(responseData)
            }
        })

        return accountDetailsLiveData
    }*/
}
sealed class AuthState
object AuthLoading: AuthState()
data class AuthTokenSuccess(val token: String): AuthState()
data class AuthSessionSuccess(val session: SessionDto): AuthState()
data class AuthDataError(val ex: DataException): AuthState()
data class AuthError(val ex: Exception): AuthState()
