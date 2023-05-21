package com.oye.moviepedia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.uses_cases.NewMovieState
import com.oye.moviepedia.domain.uses_cases.NewMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: NewMovieUseCase) : ViewModel() {

    private val _newMoviesState = MutableLiveData<NewMovieState>()
    val newMoviesState = _newMoviesState

    private val _showingMovies = MutableLiveData<MutableList<Movie>>()
    val showingMovies: LiveData<MutableList<Movie>> = _showingMovies

    private val _comingMovies = MutableLiveData<MutableList<Movie>>()
    val comingMovies: LiveData<MutableList<Movie>> = _comingMovies

    init {
        getNewMovies()
    }

    private fun getNewMovies() {
        viewModelScope.launch {
            useCase.fetchNewMovies().collect {
                _newMoviesState.value = it
            }
        }
    }
}