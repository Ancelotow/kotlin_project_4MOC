package com.oye.moviepedia.ui.user

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentDetailPlaylistBinding
import com.oye.moviepedia.domain.uses_cases.ListDetailDataError
import com.oye.moviepedia.domain.uses_cases.ListDetailError
import com.oye.moviepedia.domain.uses_cases.ListDetailSuccess
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.domain.uses_cases.MovieDetailsDataError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError


@AndroidEntryPoint
class DetailPlaylistFragment : Fragment(), MovieInPlaylistListAdapter.MovieListener{

    private val viewModel: DetailPlaylistViewModel by viewModels()
    private var _binding: FragmentDetailPlaylistBinding? = null

    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>()


    private var playlistId: Int = 0
    private var accessToken: String? = null

    val authData = SessionManager.getAuth()

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
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = gridLayoutManager

        val backButton = binding.backButton
        backButton.setOnClickListener {
            showProfileView(authData)
        }

        val deleteButton = binding.deleteButton
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        initMovies()

        return view
    }

    private fun initMovies() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            when (it) {
                is ListDetailSuccess -> {
                    var playlistName = binding.playlistName
                    playlistName.text = it.playlistDetail.name
                    var nbMovies = binding.numberOfMovies
                    nbMovies.text = it.playlistDetail.object_ids.size.toString() + " film(s)"
                    val movieIds = it.playlistDetail.object_ids.keys.toList()
                    var movieDetailsReceived = 0
                    for (movieId in movieIds) {
                        val id = movieId.substringAfter(":").toInt()
                        viewModel.getMovie(id)

                        val movies = mutableListOf<MovieItem>()  // Réinitialiser la liste à chaque itération
                        viewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
                            when (movieDetails) {
                                is MovieDetailsSuccess -> {
                                    val movieItem = MovieItem(
                                        movieDetails.movie.id,
                                        movieDetails.movie.title,
                                        movieDetails.movie.posterUrl,
                                        movieDetails.movie.director
                                    )
                                    movies.add(movieItem)
                                    movieDetailsReceived++

                                    if (movieDetailsReceived == movieIds.size) {
                                        movieList.add(ListMovieItem(movies))
                                        binding.moviesRecyclerView.adapter = ListMovieInPlaylistListAdapter(movieList, activity, this)
                                    }
                                }
                                is MovieDetailsDataError -> {
                                    Log.e("DATA ERROR", movieDetails.ex.message!!)
                                }
                                is MovieDetailsError -> {
                                    Log.e("ERROR", movieDetails.ex.message!!)
                                }
                                else -> {
                                    Log.d("log", "detail dans else : ${movieDetails}")
                                }
                            }
                        }
                    }
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
        Log.d("log", "movie id : $movieId")
        //val action =
        //findNavController().navigate(action, extras)
    }

    private fun showProfileView(authData: AuthDto) {
        val profileFragment = ProfileFragment.newInstance(authData)
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, profileFragment)
            .commit()
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Supprimer la playlist")
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir supprimer la playlist ?")
        alertDialogBuilder.setPositiveButton("Oui") { dialog, which ->
            viewModel.deletePlaylist(accessToken!!, playlistId)
            showProfileView(authData)
        }
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}