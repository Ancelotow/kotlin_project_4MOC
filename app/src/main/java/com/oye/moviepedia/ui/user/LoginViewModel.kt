package com.oye.moviepedia.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun createSession(request_token: String){
        viewModelScope.launch {
            useCaseAuth.createSession(request_token).collect { authState ->
                _authState.value = authState
            }
        }
    }

    /*fun getAccountDetails(sessionId: String, accountId: Int): MutableLiveData<AccountDetailDto> {
        return authRepository.getAccountDetails(sessionId, accountId)
    }*/
}
