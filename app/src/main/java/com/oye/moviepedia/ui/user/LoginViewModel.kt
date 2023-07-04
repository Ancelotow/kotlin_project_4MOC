package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.AuthSessionSuccess
import com.oye.moviepedia.domain.uses_cases.AuthState
import com.oye.moviepedia.domain.uses_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCaseAuth: AuthUseCase,
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState = _authState

    val sessionData = MutableLiveData<String>()

    init {
        getRequestToken()
    }

    private fun getRequestToken() {
        viewModelScope.launch {
            useCaseAuth.getRequestToken().collect {
                _authState.value = it
            }
        }
    }

    fun createSession(request_token: String) {
        viewModelScope.launch {
            useCaseAuth.createSession(request_token).collect { authState ->
                _authState.value = authState
                if (authState is AuthSessionSuccess) {
                    val sessionDto = authState.session
                    if (sessionDto != null) {
                        sessionData.value = sessionDto.session_id
                        Log.d("log", "dans view model : ${sessionData.value}")
                    } else {
                    }
                }
            }
        }
    }

    /*fun getAccountDetails(sessionId: String, accountId: Int): MutableLiveData<AccountDetailDto> {
        return authRepository.getAccountDetails(sessionId, accountId)
    }*/
}
