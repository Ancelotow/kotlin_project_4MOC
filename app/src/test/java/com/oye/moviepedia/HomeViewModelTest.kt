package com.oye.moviepedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.interactors.HomeInteractor
import com.oye.moviepedia.domain.uses_cases.NewMovieState
import com.oye.moviepedia.domain.uses_cases.NewMovieSuccess
import com.oye.moviepedia.domain.uses_cases.NewMovieUseCase
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieUseCase
import com.oye.moviepedia.domain.uses_cases.PopularMovieUseCase
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieUseCase
import com.oye.moviepedia.ui.home.HomeEvent
import com.oye.moviepedia.ui.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import kotlinx.coroutines.test.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    // Mocks des use cases
    @Mock
    private val mockNewMovieUseCase = mock<NewMovieUseCase>()
    @Mock
    private val mockNowPlayingMovieUseCase = mock<NowPlayingMovieUseCase>()
    @Mock
    private val mockUpcomingMovieUseCase = mock<UpcomingMovieUseCase>()
    @Mock
    private val mockPopularMovieUseCase = mock<PopularMovieUseCase>()

    // Le view model à tester
    private val interactor by lazy { HomeInteractor(mockNewMovieUseCase, mockNowPlayingMovieUseCase, mockPopularMovieUseCase, mockUpcomingMovieUseCase) }
    private val viewModel by lazy { HomeViewModel(interactor) }

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
    @ExperimentalCoroutinesApi
    fun `test getNewMovies`() {
        val newMovies = emptyList<Movie>()
        `when`(mockNewMovieUseCase.invoke()).thenReturn(flowOf(NewMovieSuccess(newMovies)))
        viewModel.onEventChanged(HomeEvent.OnNewMovies)
        val expectedState = NewMovieSuccess(newMovies)
        assertEquals(viewModel.newMoviesState.value, expectedState)
    }
}