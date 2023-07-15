package com.oye.moviepedia.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.interactors.DetailsInteractor
import com.oye.moviepedia.domain.uses_cases.CreateListState
import com.oye.moviepedia.domain.uses_cases.CreateListUseCase
import com.oye.moviepedia.domain.uses_cases.DeletePlaylistState
import com.oye.moviepedia.domain.uses_cases.DeletePlaylistUseCase
import com.oye.moviepedia.domain.uses_cases.GetListsState
import com.oye.moviepedia.domain.uses_cases.GetListsUseCase
import com.oye.moviepedia.domain.uses_cases.LikedMovieState
import com.oye.moviepedia.domain.uses_cases.LikedMovieUseCase
import com.oye.moviepedia.domain.uses_cases.ListDetailState
import com.oye.moviepedia.domain.uses_cases.MovieDetailsState
import com.oye.moviepedia.domain.uses_cases.PlaylistDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPlaylistViewModel @Inject constructor(
    private val useCaseGetDetailList: PlaylistDetailUseCase,
    private val useCaseDeletePlaylist: DeletePlaylistUseCase,
    private val detailsInteractor: DetailsInteractor

) : ViewModel() {

    private var accessToken: String? = null
    private var listId: Int? = 0

    private val _detailPlaylistState = MutableLiveData<ListDetailState>()
    val playlistState = _detailPlaylistState

    private val _deletePlaylistState = MutableLiveData<DeletePlaylistState>()
    val deletePlaylistState = _deletePlaylistState

    private val _movieDetails = MutableLiveData<MovieDetailsState>()
    val movieDetails: LiveData<MovieDetailsState> = _movieDetails

    fun init (accessToken: String?, listId: Int?){
        this.accessToken = accessToken
        this.listId = listId
        getPlaylistDetail(accessToken!!, listId!!)
    }

    private fun getPlaylistDetail(token: String, listId: Int) {
        viewModelScope.launch {
            useCaseGetDetailList.getList(token, listId).collect {
                _detailPlaylistState.value = it
            }
        }
    }

    fun getMovie(id: Int) {
        viewModelScope.launch {
            detailsInteractor.movieDetailsUseCase.getMovie(id).collect {
                _movieDetails.value = it
            }
        }
    }

    fun deletePlaylist(token: String, listId: Int) {
        viewModelScope.launch {
            useCaseDeletePlaylist.deletePlaylist(token, listId).collect {
                _deletePlaylistState.value = it
            }
        }
    }
}