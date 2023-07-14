package com.oye.moviepedia.ui.user

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.databinding.FragmentProfileBinding
import com.oye.moviepedia.domain.entities.Playlist
import com.oye.moviepedia.domain.uses_cases.LikedMovieDataError
import com.oye.moviepedia.domain.uses_cases.LikedMovieError
import com.oye.moviepedia.domain.uses_cases.LikedMovieSuccess
import com.oye.moviepedia.ui.home.ListMovieItem
import com.oye.moviepedia.ui.home.ListMovieListAdapter
import com.oye.moviepedia.ui.home.MovieItem
import com.oye.moviepedia.ui.home.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), MovieListAdapter.MovieListener {

    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>(4).apply {
        repeat(4) {
            add(ListMovieItem("", mutableListOf()))
        }
    }

    private val playlistList = ArrayList<ListPlaylistItem>(4).apply {
        repeat(4) {
            add(ListPlaylistItem(mutableListOf()))
        }
    }

    private var accountId: String? = null
    private var accessToken: String? = null

    private lateinit var validateButton: Button
    private lateinit var editText: EditText

    companion object {
        private const val ARG_ACCOUNT_ID = "account_id"
        private const val ARG_ACCESS_TOKEN = "access_token"

        fun newInstance(authData: AuthDto): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_ACCOUNT_ID, authData.account_id)
            args.putString(ARG_ACCESS_TOKEN, authData.access_token)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            accountId = it.getString(ARG_ACCOUNT_ID)
            accessToken = it.getString(ARG_ACCESS_TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        validateButton = binding.validateButton
        editText = binding.editTextPlaylist

        val title = binding.appTitle
        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        title.paint.shader = LinearGradient(
            0f, 0f, width, 0f, Color.parseColor("#9CCCA5"), Color.parseColor("#51B1DF"),
            Shader.TileMode.CLAMP
        )

        val recyclerView = binding.recyclerLikedMovies
        val linearLayoutManager = LinearLayoutManager(container?.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        val recyclerViewPlaylist = binding.recyclerPlaylist
        val playlistLinearLayoutManager = LinearLayoutManager(container?.context)
        recyclerViewPlaylist.layoutManager = playlistLinearLayoutManager

        movieList.ensureCapacity(4)
        playlistList.ensureCapacity(4)
        initLikedMovies()
        initPlaylist()

        val buttonAddPlaylist = binding.addButtonPlaylist
        buttonAddPlaylist.setOnClickListener {
            Log.d("log", "on click avant fonction")
            onAddButtonClick()
        }

        val logoutButton = binding.logoutButton
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        validateButton.setOnClickListener {
            val playlistName = editText.text.toString()
            Log.d("log", "access token : $accessToken")
            accessToken?.let { it1 -> viewModel.createPlaylist(it1, playlistName) }
            editText.isVisible = false
            validateButton.isVisible = false
        }

        return view
    }

    private fun initLikedMovies() {
        viewModel.likedMoviesState.observe(viewLifecycleOwner) {
            when (it) {
                is LikedMovieSuccess -> {
                    val movies = it.movies.map { e -> MovieItem(e.id,e.title, e.posterUrl, e.director) }
                        .toMutableList()
                    movieList[0] = ListMovieItem(getString(R.string.liked_new_movies), movies)
                    binding.recyclerLikedMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is LikedMovieDataError -> {
                    Log.e("DATA ERROR", it.ex.message)
                }

                is LikedMovieError -> {
                    Log.e("ERROR", it.ex.message!!)
                }

                else -> {
                }
            }
        }
    }

    private fun initPlaylist(){
        val exemple_playlists = listOf(
            Playlist("Playlist 1", 3),
            Playlist("Playlist 2", 2),
            Playlist("Playlist 3", 2),
            Playlist("Playlist 4", 2),
        )
        val playlists = exemple_playlists.map { e -> PlaylistItem(e.name, e.nbMovies.toString()) }
            .toMutableList()
        playlistList.add(ListPlaylistItem(playlists))
        binding.recyclerPlaylist.adapter = ListPlaylistListAdapter(playlistList, activity)
    }

    private fun onAddButtonClick() {
        Log.d("log", "on click dans fonction")
        if (!editText.isVisible) {
            editText.isVisible = true
            validateButton.isVisible = true
        } else {
            editText.isVisible = false
            validateButton.isVisible = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieCLick(movieId: Int) {
        //val action =
        //findNavController().navigate(action, extras)
    }

    private fun navigateToLoginFragment() {
        val parentFragmentManager = parentFragmentManager
        val loginFragment = LoginFragment()

        parentFragmentManager.beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()

        parentFragmentManager.beginTransaction()
            .replace(R.id.container, loginFragment)
            .commit()
    }

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Déconnexion")
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
        alertDialogBuilder.setPositiveButton("Déconnexion") { dialog, which ->
            SessionManager.logout()
            navigateToLoginFragment()
        }
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}