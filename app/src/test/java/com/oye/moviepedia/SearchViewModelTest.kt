package com.oye.moviepedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.interactors.SearchInteractor
import com.oye.moviepedia.domain.uses_cases.SearchError
import com.oye.moviepedia.domain.uses_cases.SearchLoading
import com.oye.moviepedia.domain.uses_cases.SearchSuccess
import com.oye.moviepedia.domain.uses_cases.SearchUseCase
import com.oye.moviepedia.ui.search.SearchEvent
import com.oye.moviepedia.ui.search.SearchViewModel
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    // Mocks des use cases
    @Mock
    private val mockSearchUseCase = mock<SearchUseCase>()

    // Le view model à tester
    private val interactor by lazy { SearchInteractor(mockSearchUseCase) }
    private val viewModel by lazy { SearchViewModel(interactor) }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test get Search result with success`() {
        val searchResults = emptyList<SearchResult>()
        val search = "test search"
        `when`(mockSearchUseCase.invoke(search)).thenReturn(flowOf(SearchSuccess(searchResults)))
        viewModel.onEventChanged(SearchEvent.OnSearchMovies(search))
        val expectedState = SearchSuccess(searchResults)
        TestCase.assertEquals(viewModel.searchState.value, expectedState)
    }

    @Test
    fun `test get Search result with error`() {
        val exception = Exception("Test exception")
        val search = "test search"
        `when`(mockSearchUseCase.invoke(search)).thenReturn(flowOf(SearchError(exception)))
        viewModel.onEventChanged(SearchEvent.OnSearchMovies(search))
        val expectedState = SearchError(exception)
        TestCase.assertEquals(viewModel.searchState.value, expectedState)
    }

    @Test
    fun `test get Search result loading`() {
        val search = "test search"
        `when`(mockSearchUseCase.invoke(search)).thenReturn(flowOf(SearchLoading))
        viewModel.onEventChanged(SearchEvent.OnSearchMovies(search))
        val expectedState = SearchLoading
        TestCase.assertEquals(viewModel.searchState.value, expectedState)
    }

}