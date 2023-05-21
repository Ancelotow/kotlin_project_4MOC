package com.oye.moviepedia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.uses_cases.NewMovieState
import com.oye.moviepedia.domain.uses_cases.NewMovieUseCase
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieState
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCaseNewMovie: NewMovieUseCase,
    private val useCaseNowPlayingMovie: NowPlayingMovieUseCase,
) : ViewModel() {

    private val _newMoviesState = MutableLiveData<NewMovieState>()
    val newMoviesState = _newMoviesState

    private val _nowPlayingMovies = MutableLiveData<NowPlayingMovieState>()
    val nowPlayingMovies: LiveData<NowPlayingMovieState> = _nowPlayingMovies

    private val _comingMovies = MutableLiveData<MutableList<Movie>>()
    val comingMovies: LiveData<MutableList<Movie>> = _comingMovies

    init {
        getNewMovies()
        getNowPlayingMovies()
    }

    private fun getNewMovies() {
        viewModelScope.launch {
            useCaseNewMovie.fetchNewMovies().collect {
                _newMoviesState.value = it
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            useCaseNowPlayingMovie.fetchNowPlayingMovies().collect {
                _nowPlayingMovies.value = it
            }
        }
    }
}