package com.oye.moviepedia.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentDetailPlaylistBinding
import com.oye.moviepedia.domain.uses_cases.ListDetailDataError
import com.oye.moviepedia.domain.uses_cases.ListDetailError
import com.oye.moviepedia.domain.uses_cases.ListDetailSuccess
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import com.oye.moviepedia.ui.home.ListMovieItem
import com.oye.moviepedia.ui.home.ListMovieListAdapter
import com.oye.moviepedia.ui.home.MovieListAdapter
import com.oye.moviepedia.ui.home.MovieItem
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.oye.moviepedia.domain.uses_cases.MovieDetailsState


@AndroidEntryPoint
class DetailPlaylistFragment : Fragment(), MovieListAdapter.MovieListener{

    private val viewModel: DetailPlaylistViewModel by viewModels()
    private var _binding: FragmentDetailPlaylistBinding? = null

    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>(4).apply {
        repeat(4) {
            add(ListMovieItem("", mutableListOf()))
        }
    }

    private var playlistId: Int = 0
    private var accessToken: String? = null

    companion object {
        private const val ARG_PLAYLIST_ID = 0
        private const val ARG_ACCESS_TOKEN = "access_token"


        fun newInstance(playlistId: Int, accessToken: String): DetailPlaylistFragment {
            val fragment = DetailPlaylistFragment()
            val args = Bundle()
            args.putInt(ARG_PLAYLIST_ID.toString(), playlistId)
            args.putString(ARG_ACCESS_TOKEN, accessToken)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getInt(ARG_PLAYLIST_ID.toString())
            accessToken = it.getString(ARG_ACCESS_TOKEN)
        }
        viewModel.init(accessToken, playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPlaylistBinding.inflate(inflater, container, false)
        val view = binding.root


        val recyclerView = binding.moviesRecyclerView
        val linearLayoutManager = LinearLayoutManager(container?.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        movieList.ensureCapacity(4)
        initMovies()


        val deleteButton = binding.deleteButton
        deleteButton.setOnClickListener {
            //showDeleteConfirmationDialog()
        }

        return view
    }

    private fun initMovies() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            when (it) {
                is ListDetailSuccess -> {
                    val movies = mutableListOf<MovieItem>()
                    for (movie in it.playlistDetail.object_ids.keys) {
                        val movieId = movie.substringAfter(":")
                        Log.d("log", "detail : $movieId")
                        viewModel.getMovie(movieId.toInt())

                        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { movieDetails: MovieDetailsState ->
                            when (movieDetails) {
                                is MovieDetailsSuccess -> {
                                    val movieItem = MovieItem(
                                        movieDetails.movie.id,
                                        movieDetails.movie.title,
                                        movieDetails.movie.posterUrl,
                                        movieDetails.movie.director
                                    )
                                    movies.add(movieItem)
                                }
                                else -> {}
                            }
                        })
                    }
                    movieList[0] = ListMovieItem(getString(R.string.liked_new_movies), movies)
                    binding.moviesRecyclerView.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is ListDetailDataError -> {
                    Log.e("DATA ERROR", it.ex.message)
                }

                is ListDetailError -> {
                    Log.e("ERROR", it.ex.message!!)
                }

                else -> {
                }
            }
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


    /*private fun showLogoutConfirmationDialog() {
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
    }*/

}