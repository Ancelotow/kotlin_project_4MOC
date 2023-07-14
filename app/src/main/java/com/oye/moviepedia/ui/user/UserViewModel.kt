package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.CreateListState
import com.oye.moviepedia.domain.uses_cases.CreateListUseCase
import com.oye.moviepedia.domain.uses_cases.LikedMovieState
import com.oye.moviepedia.domain.uses_cases.LikedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCaseLikedMovie: LikedMovieUseCase,
    private val useCaseCreateList: CreateListUseCase,
    ) : ViewModel() {

    private val _likedMoviesState = MutableLiveData<LikedMovieState>()
    val likedMoviesState = _likedMoviesState

    private val _createListState = MutableLiveData<CreateListState>()
    val createListState = _createListState

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

    fun createPlaylist(token: String, name: String) {
        viewModelScope.launch {
            useCaseCreateList.createList(token, name).collect {
                _createListState.value = it
            }
        }
    }
}