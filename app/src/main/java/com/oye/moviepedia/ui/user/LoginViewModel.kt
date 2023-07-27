package com.oye.moviepedia.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.domain.uses_cases.AuthState
import com.oye.moviepedia.domain.uses_cases.AuthSuccess
import com.oye.moviepedia.domain.interactors.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val interactor: LoginInteractor,
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState = _authState

    val authData = MutableLiveData<AuthDto>()

    fun onEventChanged(event: LoginEvent){
        when(event){
            LoginEvent.OnGetToken -> getRequestToken()
        }
    }

    init {
        getRequestToken()
    }

    private fun getRequestToken() {
        viewModelScope.launch {
            interactor.useCaseAuth.invoke().collect {
                _authState.value = it
            }
        }
    }

    fun getAccountId(requestToken: String){
        viewModelScope.launch {
            interactor.useCaseAuth.getAccountId(requestToken).collect { authState ->
                _authState.value = authState
                if (authState is AuthSuccess) {
                    val authDto = authState.auth
                    if (authDto != null) {
                        authData.value = authDto
                    } else {
                    }
                }
            }
        }
    }
}
