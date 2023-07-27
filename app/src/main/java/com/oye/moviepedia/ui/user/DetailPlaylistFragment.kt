package com.oye.moviepedia.ui.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentDetailPlaylistBinding
import com.oye.moviepedia.domain.uses_cases.ListDetailDataError
import com.oye.moviepedia.domain.uses_cases.ListDetailError
import com.oye.moviepedia.domain.uses_cases.ListDetailSuccess
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.GridLayoutManager
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems
import com.oye.moviepedia.domain.uses_cases.MovieDetailsDataError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.ui.BaseFragment


@AndroidEntryPoint
class DetailPlaylistFragment : BaseFragment(), MovieInPlaylistListAdapter.MovieListener, MovieInPlaylistListAdapter.MovieLongListener{

    private val viewModel: DetailPlaylistViewModel by viewModels()
    private var _binding: FragmentDetailPlaylistBinding? = null
    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>()
    private var playlistId: Int = 0
    private var accessToken: String? = null
    private val args: DetailPlaylistFragmentArgs by navArgs()
    val authData = SessionManager.getAuth()
    lateinit var playlistName: TextView

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
        playlistId = args.playlistID
        accessToken = args.accessToken
        viewModel.init(accessToken, playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailPlaylistBinding.inflate(inflater, container, false)
        val view = binding.root
        setupUI()

        val recyclerView = binding.moviesRecyclerView
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = gridLayoutManager

        initMovies()

        return view
    }

    private fun setupUI() {
        setupSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.setNavigationOnClickListener {
            onSupportNavigateUp()
        }
        setupMenu()
    }

    private fun setupMenu() {
        val menuResId : Int = R.menu.playlist_details_menu
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            @SuppressLint("RestrictedApi")
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
                if (menu is MenuBuilder) {
                    menu.setOptionalIconsVisible(true)
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuResId, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_delete -> {
                        if(playlistName.text.equals("A voir") || playlistName.text.equals("Favoris")){
                            showInformationDialog()
                        }else{
                            showDeleteConfirmationDialog()
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initMovies() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            when (it) {
                is ListDetailSuccess -> {
                    playlistName = binding.playlistName
                    playlistName.text = it.playlistDetail.name
                    var nbMovies = binding.numberOfMovies
                    nbMovies.text = "${it.playlistDetail.object_ids.size} film(s)"
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
                                        binding.moviesRecyclerView.adapter = ListMovieInPlaylistListAdapter(movieList, activity, this, this)
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
        val action = DetailPlaylistFragmentDirections.detailsFragmentAction(movieId)
        findNavController().navigate(action)
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

    private fun showInformationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Suppression impossible")
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onMovieLongClick(movieId: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Supprimer le film de la playlist")
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir retirer le film ?")
        alertDialogBuilder.setPositiveButton("Oui") { dialog, which ->
            val item = listOf(Item("movie", movieId))
            val listItems = ListItems(items = item)
            viewModel.removeMovie(accessToken!!, playlistId, listItems)
        }
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}