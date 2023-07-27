package com.oye.moviepedia.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.interactors.SearchInteractor
import com.oye.moviepedia.domain.uses_cases.SearchState
import com.oye.moviepedia.domain.uses_cases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val interactor: SearchInteractor,
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState = _searchState

    fun onEventChanged(event: SearchEvent){
        when(event){
            is SearchEvent.OnSearchMovies -> getSearchResult(event.search)
            else -> {}
        }
    }

    private fun getSearchResult(query: String) {
        viewModelScope.launch {
            interactor.searchUseCase.invoke(query).collect {
                _searchState.value = it
            }
        }
    }
}