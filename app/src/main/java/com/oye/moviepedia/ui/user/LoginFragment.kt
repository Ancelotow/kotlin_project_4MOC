package com.oye.moviepedia.ui.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.oye.moviepedia.databinding.FragmentLoginBinding
import com.oye.moviepedia.domain.uses_cases.AuthDataError
import com.oye.moviepedia.domain.uses_cases.AuthError
import com.oye.moviepedia.domain.uses_cases.AuthSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var userFragment: UserFragment? = null
    private var approvedRequestToken: String? = null
    private var isAuthenticated = false



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        userFragment = parentFragment as? UserFragment

        val title = binding.appTitle
        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        title.paint.shader = LinearGradient(
            0f, 0f, width, 0f, Color.parseColor("#9CCCA5"), Color.parseColor("#51B1DF"),
            Shader.TileMode.CLAMP
        )

        val login = binding.usernameEditText
        val password = binding.passwordEditText
        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            loginButton.setOnClickListener {
                val username = login.text.toString()
                val password = password.text.toString()

                viewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
                    when (authState) {
                        is AuthSuccess -> {
                            val token = authState.token
                            if (token != null) {
                                authenticateWithToken(token)
                                approvedRequestToken = token
                                isAuthenticated = true
                                Log.d("log", "dans model")

                            }  else {
                                Log.d("Token erreur", "erreur 1")
                            }
                        }
                        is AuthDataError -> {
                            Log.e("DATA ERROR", authState.ex.message)
                        }
                        is AuthError -> {
                            Log.e("ERROR", authState.ex.message!!)
                        }
                        else -> {
                            // Handle other cases if needed
                        }
                    }
                })

            }
        }


        return view
    }

    private fun authenticateWithToken(requestToken: String) {
        val authenticationUrl = "https://www.themoviedb.org/authenticate/$requestToken"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authenticationUrl))
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("log", "avant is authenticated")
        if (resultCode == RESULT_OK && isAuthenticated) {
            Log.d("log", "dans is authenticated")
            approvedRequestToken?.let { viewModel.createSession(it) }
        }
    }



}