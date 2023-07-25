package com.oye.moviepedia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.interactors.HomeInteractor
import com.oye.moviepedia.domain.uses_cases.NewMovieState
import com.oye.moviepedia.domain.uses_cases.NewMovieUseCase
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieState
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieUseCase
import com.oye.moviepedia.domain.uses_cases.PopularMovieState
import com.oye.moviepedia.domain.uses_cases.PopularMovieUseCase
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieState
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: HomeInteractor,
) : ViewModel() {

    private val _newMoviesState = MutableLiveData<NewMovieState>()
    val newMoviesState = _newMoviesState

    private val _nowPlayingMovies = MutableLiveData<NowPlayingMovieState>()
    val nowPlayingMovies: LiveData<NowPlayingMovieState> = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<UpcomingMovieState>()
    val upcomingMovies: LiveData<UpcomingMovieState> = _upcomingMovies

    private val _popularMovies = MutableLiveData<PopularMovieState>()
    val popularMovies: LiveData<PopularMovieState> = _popularMovies

    init {
        getNewMovies()
        getNowPlayingMovies()
        getUpcomingMovies()
        getPopularMovies()
    }

    private fun getNewMovies() {
        viewModelScope.launch {
            interactor.newMovieUseCase.fetchNewMovies().collect {
                _newMoviesState.value = it
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            interactor.nowPlayingMovieUseCase.fetchNowPlayingMovies().collect {
                _nowPlayingMovies.value = it
            }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            interactor.upcomingMovieUseCase.fetchUpcomingMovies().collect {
                _upcomingMovies.value = it
            }
        }
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            interactor.popularMovieUseCase.fetchPopularMovies().collect {
                _popularMovies.value = it
            }
        }
    }
}