package com.oye.moviepedia.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oye.moviepedia.R

class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        if (SessionManager.isLoggedIn()) {
            // Utilisateur connecté, affichez la vue du profil
            val accountId = SessionManager.getAccountId()
            showProfileView(accountId)
        } else {
            // Utilisateur non connecté, affichez la vue de connexion
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

    private fun showProfileView(accountId: String?) {
        val profileFragment = ProfileFragment.newInstance(accountId)
        childFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment)
            .commit()
    }

}