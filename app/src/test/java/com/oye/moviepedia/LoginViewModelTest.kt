package com.oye.moviepedia

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.oye.moviepedia.domain.interactors.LoginInteractor
import com.oye.moviepedia.domain.uses_cases.AuthError
import com.oye.moviepedia.domain.uses_cases.AuthLoading
import com.oye.moviepedia.domain.uses_cases.AuthTokenSuccess
import com.oye.moviepedia.domain.uses_cases.AuthUseCase
import com.oye.moviepedia.ui.user.LoginEvent
import com.oye.moviepedia.ui.user.LoginViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import kotlinx.coroutines.test.*
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    // Mocks des use cases
    @Mock
    private val mockAuthUseCase = mock<AuthUseCase>()

    // Le view model à tester
    private val interactor by lazy { LoginInteractor(mockAuthUseCase) }
    private val viewModel by lazy { LoginViewModel(interactor) }

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
    fun `test get Token with success`() {
        `when`(mockAuthUseCase.invoke()).thenReturn(flowOf(
            AuthTokenSuccess("token")
        ))
        viewModel.onEventChanged(LoginEvent.OnGetToken)
        val expectedState = AuthTokenSuccess("token")
        assertEquals(viewModel.authState.value, expectedState)
    }

    @Test
    fun `test get Token with error`() {
        val exception = Exception("Test error")
        `when`(mockAuthUseCase.invoke()).thenReturn(flowOf(AuthError(exception)))
        viewModel.onEventChanged(LoginEvent.OnGetToken)
        val expectedState = AuthError(exception)
        assertEquals(viewModel.authState.value, expectedState)
    }

    @Test
    fun `test get Token loading`() {
        `when`(mockAuthUseCase.invoke()).thenReturn(flowOf(AuthLoading))
        viewModel.onEventChanged(LoginEvent.OnGetToken)
        val expectedState = AuthLoading
        assertEquals(viewModel.authState.value, expectedState)
    }
}