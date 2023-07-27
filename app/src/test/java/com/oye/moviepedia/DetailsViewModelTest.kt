package com.oye.moviepedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.interactors.DetailsInteractor
import com.oye.moviepedia.domain.uses_cases.AddMovieToPlaylistUseCase
import com.oye.moviepedia.domain.uses_cases.GetListsUseCase
import com.oye.moviepedia.domain.uses_cases.GetMovieDynamicLinkUseCase
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsLoading
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import com.oye.moviepedia.domain.uses_cases.MovieDetailsUseCase
import com.oye.moviepedia.ui.details.DetailsScreenEvent
import com.oye.moviepedia.ui.details.DetailsViewModel
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
import org.mockito.Mockito
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    // Mocks des use cases
    @Mock
    private val mockMovieDetailsUseCase = mock<MovieDetailsUseCase>()
    @Mock
    private val mockAddMovieToPlaylistUseCase = mock<AddMovieToPlaylistUseCase>()
    @Mock
    private val mockGetListsUseCase = mock<GetListsUseCase>()
    @Mock
    private val mockGetMovieDynamicLinkUseCase = mock<GetMovieDynamicLinkUseCase>()

    // Le view model à tester
    private val interactor by lazy { DetailsInteractor(mockMovieDetailsUseCase, mockAddMovieToPlaylistUseCase, mockGetListsUseCase, mockGetMovieDynamicLinkUseCase) }
    private val viewModel by lazy { DetailsViewModel(interactor) }

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
    fun `test get Movie details with success`() {
        val movieDetails = MovieDetails(
            10,
        "movie",
        "",
        "",
        "",
        5f,
        5,
        LocalDate.now(),
        false,
        null,
        null,
        emptyList(),
        null,
        ""
        )

        Mockito.`when`(mockMovieDetailsUseCase.invoke(10)).thenReturn(flowOf(MovieDetailsSuccess(movieDetails)))
        viewModel.onEventChanged(DetailsScreenEvent.OnGetMovie(10))
        val expectedState = MovieDetailsSuccess(movieDetails)
        TestCase.assertEquals(viewModel.movieDetails.value, expectedState)
    }

    @Test
    fun `test get Movie details with error`() {
        val exception = Exception("Test error")
        Mockito.`when`(mockMovieDetailsUseCase.invoke(10)).thenReturn(flowOf(MovieDetailsError(exception)))
        viewModel.onEventChanged(DetailsScreenEvent.OnGetMovie(10))
        val expectedState = MovieDetailsError(exception)
        TestCase.assertEquals(viewModel.movieDetails.value, expectedState)
    }

    @Test
    fun `test get Movie details with loading`() {
        Mockito.`when`(mockMovieDetailsUseCase.invoke(10)).thenReturn(flowOf(MovieDetailsLoading))
        viewModel.onEventChanged(DetailsScreenEvent.OnGetMovie(10))
        val expectedState = MovieDetailsLoading
        TestCase.assertEquals(viewModel.movieDetails.value, expectedState)
    }
}