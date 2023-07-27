package com.oye.moviepedia.domain.uses_cases

import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke(): Flow<AuthState> {
        return flow {
            emit(AuthLoading)
            try {
                emit(AuthTokenSuccess(repository.getRequestToken()))
            } catch (e: Exception) {
                emit(AuthError(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAccountId(request_token: String): Flow<AuthState> {
        return flow {
            emit(AuthLoading)
            try {
                emit(AuthSuccess(repository.getAccountId(request_token)))
            } catch (e: Exception) {
                emit(AuthError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}
sealed class AuthState
object AuthLoading: AuthState()
data class AuthTokenSuccess(val token: String): AuthState()
data class AuthSuccess(val auth: AuthDto): AuthState()
data class AuthError(val ex: Exception): AuthState()
