package com.oye.moviepedia.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.LikedMovieState
import com.oye.moviepedia.domain.uses_cases.LikedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCaseLikedMovie: LikedMovieUseCase,
) : ViewModel() {

    private val _likedMoviesState = MutableLiveData<LikedMovieState>()
    val likedMoviesState = _likedMoviesState

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
}