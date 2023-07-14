package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.CreateListState
import com.oye.moviepedia.domain.uses_cases.CreateListUseCase
import com.oye.moviepedia.domain.uses_cases.GetListsState
import com.oye.moviepedia.domain.uses_cases.GetListsUseCase
import com.oye.moviepedia.domain.uses_cases.LikedMovieState
import com.oye.moviepedia.domain.uses_cases.LikedMovieUseCase
import com.oye.moviepedia.domain.uses_cases.ListDetailState
import com.oye.moviepedia.domain.uses_cases.PlaylistDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCaseLikedMovie: LikedMovieUseCase,
    private val useCaseCreateList: CreateListUseCase,
    private val useCaseGetLists: GetListsUseCase,
) : ViewModel() {

    private var accessToken: String? = null
    private var accountId: String? = null

    private val _likedMoviesState = MutableLiveData<LikedMovieState>()
    val likedMoviesState = _likedMoviesState

    private val _createListState = MutableLiveData<CreateListState>()
    val createListState = _createListState

    private val _getListsState = MutableLiveData<GetListsState>()
    val getListsState = _getListsState

    fun init (accessToken: String?, accountId: String?){
        this.accessToken = accessToken
        this.accountId = accountId
        getLikedMovies()
        getLists()
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

    private fun getLists() {
        Log.d("log", "access token : $accessToken, account id : $accountId")
        if (accessToken != null && accountId != null) {
            Log.d("log", "dans if view model")
            viewModelScope.launch {
                useCaseGetLists.getLists(accessToken!!, accountId!!).collect {
                    _getListsState.value = it
                }
            }
        } else {
            Log.d("log", "dans else view model")
        }
    }
}