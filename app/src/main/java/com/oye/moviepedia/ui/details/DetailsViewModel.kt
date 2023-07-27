package com.oye.moviepedia.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.interactors.DetailsInteractor
import com.oye.moviepedia.domain.uses_cases.GetMovieDynamicLinkState
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsInteractor: DetailsInteractor
) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetailsState>()
    val movieDetails: LiveData<MovieDetailsState> = _movieDetails

    private val _dynamicLink = MutableLiveData<GetMovieDynamicLinkState>()
    val dynamicLink: LiveData<GetMovieDynamicLinkState> = _dynamicLink

    fun onEventChanged(event: DetailsScreenEvent) {
        when (event) {
            is DetailsScreenEvent.OnGetMovie -> {
                getMovie(event.movieId)
            }
            is DetailsScreenEvent.OnGetDynamicLink -> {
                getDynamicLink(event.movie)
            }
        }
    }

    private fun getMovie(id: Int) {
        viewModelScope.launch {
            detailsInteractor.movieDetailsUseCase.getMovie(id).collect {
                _movieDetails.value = it
            }
        }
    }

    private fun getDynamicLink(movie: MovieDetails) {
        viewModelScope.launch {
            detailsInteractor.getMovieDynamicLinkUseCase.invoke(movie).collect {
                _dynamicLink.value = it
            }
        }
    }
}