package com.oye.moviepedia.domain.uses_cases

import android.util.Log
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.exceptions.DataException
import com.oye.moviepedia.domain.repositories.AuthRepository
import com.oye.moviepedia.domain.repositories.LogoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: LogoutRepository) {

    fun logout(access_token: String): Flow<LogoutState> {
        return flow {
            emit(LogoutLoading)
            try {
                emit(LogoutSuccess(repository.logout(access_token)))
            } catch (e: DataException) {
                emit(LogoutDataError(e))
            }  catch (e: Exception) {
                emit(LogoutError(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}
sealed class LogoutState
object LogoutLoading: LogoutState()
data class LogoutSuccess(val response: Boolean): LogoutState()
data class LogoutDataError(val ex: DataException): LogoutState()
data class LogoutError(val ex: Exception): LogoutState()
