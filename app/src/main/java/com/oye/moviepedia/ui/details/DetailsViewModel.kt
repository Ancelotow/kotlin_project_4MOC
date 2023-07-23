package com.oye.moviepedia.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.NewItem
import com.oye.moviepedia.domain.interactors.DetailsInteractor
import com.oye.moviepedia.domain.uses_cases.AddMovieState
import com.oye.moviepedia.domain.uses_cases.GetListsState
import com.oye.moviepedia.domain.uses_cases.MovieDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsInteractor: DetailsInteractor
) : ViewModel() {
    private var accessToken: String? = null
    private var accountId: String? = null

    private val _movieDetails = MutableLiveData<MovieDetailsState>()
    val movieDetails: LiveData<MovieDetailsState> = _movieDetails

    private val _addMovie = MutableLiveData<AddMovieState>()
    val addMovie: LiveData<AddMovieState> = _addMovie

    private val _getListsState = MutableLiveData<GetListsState>()
    val getLists: LiveData<GetListsState> = _getListsState

    fun init (accessToken: String?, accountId: String?){
        this.accessToken = accessToken
        this.accountId = accountId
        getLists()
    }

    fun getMovie(id: Int) {
        viewModelScope.launch {
            detailsInteractor.movieDetailsUseCase.getMovie(id).collect {
                _movieDetails.value = it
            }
        }
    }

    fun addMovie(token: String, listId: Int, newItem: List<NewItem>) {
        viewModelScope.launch {
            detailsInteractor.useCaseAddMovieToPlaylist.addMovie(token, listId, newItem).collect {
                _addMovie.value = it
            }
        }
    }

    private fun getLists() {
        Log.d("log", "access token : $accessToken, account id : $accountId")
        if (accessToken != null && accountId != null) {
            Log.d("log", "dans if view model")
            viewModelScope.launch {
                detailsInteractor.useCaseGetLists.getLists(accessToken!!, accountId!!).collect {
                    _getListsState.value = it
                }
            }
        } else {
            Log.d("log", "dans else view model")
        }
    }

}