package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.LikedMovieState
import com.oye.moviepedia.domain.uses_cases.LikedMovieUseCase
import com.oye.moviepedia.domain.uses_cases.LogoutState
import com.oye.moviepedia.domain.uses_cases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCaseLikedMovie: LikedMovieUseCase,
    private val useCaseLogout: LogoutUseCase,

    ) : ViewModel() {

    private val _likedMoviesState = MutableLiveData<LikedMovieState>()
    val likedMoviesState = _likedMoviesState

    private val _logoutState = MutableLiveData<LogoutState>()

    init {
        getLikedMovies()
    }

    private fun getLikedMovies() {
        viewModelScope.launch {
            useCaseLikedMovie.fetchNowPlayingMovies().collect {
                _likedMoviesState.value = it
            }
        }
    }

    fun logout(access_token: String) {
        Log.d("log", "dans view model : $access_token")
        viewModelScope.launch {
            useCaseLogout.logout(access_token).collect {
                _logoutState.value = it
            }
        }
    }
}