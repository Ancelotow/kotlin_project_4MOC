package com.oye.moviepedia.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.uses_cases.SearchState
import com.oye.moviepedia.domain.uses_cases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState = _searchState

    fun getSearchResult(query: String) {
        viewModelScope.launch {
            searchUseCase.fetchSearchResult(query).collect {
                _searchState.value = it
            }
        }
    }
}