package com.oye.moviepedia.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oye.moviepedia.R
import com.oye.moviepedia.data.dto.AuthDto

class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        if (SessionManager.isLoggedIn()) {
            val authData = SessionManager.getAuth()
            showProfileView(authData)
        } else {
            showLoginView()
        }

        return view
    }

    private fun showLoginView() {
        val loginFragment = LoginFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.container, loginFragment)
            .commit()
    }

    private fun showProfileView(authData: AuthDto) {
        val profileFragment = ProfileFragment.newInstance(authData)
        childFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment)
            .commit()
    }

    fun navigateToDetailPlaylist(playlistId: Int, accessToken: String) {
        val action = UserFragmentDirections.actionUserFragmentToDetailPlaylistFragment(playlistId, accessToken)
        findNavController().navigate(action)
    }

}