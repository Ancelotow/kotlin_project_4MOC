package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.AuthState
import com.oye.moviepedia.domain.uses_cases.AuthSuccess
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

    val authData = MutableLiveData<String>()

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

    fun getAccountId(requestToken: String){
        viewModelScope.launch {
            useCaseAuth.getAccountId(requestToken).collect { authState ->
                _authState.value = authState
                if (authState is AuthSuccess) {
                    val authDto = authState.auth
                    if (authDto != null) {
                        authData.value = authDto.account_id
                        Log.d("log", "account ID dans view model : ${authData.value}")
                    } else {
                    }
                }
            }
        }
    }
}
