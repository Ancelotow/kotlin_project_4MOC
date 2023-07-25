package com.oye.moviepedia.ui.details

import SessionManager
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentDetailsBinding
import com.oye.moviepedia.domain.entities.Genre
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems
import com.oye.moviepedia.domain.uses_cases.GetListsDataError
import com.oye.moviepedia.domain.uses_cases.GetListsError
import com.oye.moviepedia.domain.uses_cases.GetListsSuccess
import com.oye.moviepedia.domain.uses_cases.MovieDetailsDataError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsLoading
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import com.oye.moviepedia.ui.BaseFragment
import com.oye.moviepedia.ui.user.ListPlaylistItem
import com.oye.moviepedia.ui.user.PlaylistItem
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: BaseFragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var movie: MovieDetails
    private lateinit var actorAdapter: ActorsAdapter
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val args : DetailsFragmentArgs by navArgs()
    private val playlistList = ArrayList<ListPlaylistItem>(4).apply {
        repeat(4) {
            add(ListPlaylistItem(mutableListOf()))
        }
    }
    val authData = SessionManager.getAuth()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MovieId","${args.movieId}")

        setupUI()
        setObservers()
        setUIListeners()
        detailsViewModel.getMovie(args.movieId)
    }

    private fun setupUI() {
        setupSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.setNavigationOnClickListener {
            onSupportNavigateUp()
        }
        setupMenu()
    }

    private fun setMovieData() {
        binding.movieTitle.text = movie.title
        binding.movieDescription.text = movie.description
        Picasso.get().load(movie.posterUrl).into(binding.moviePicture)

        val goodRatePercentage = (movie.noteAverage * 10).toInt()
        binding.rateValue.text = "${goodRatePercentage}%"
        binding.rateProgressIndicator.progress = goodRatePercentage

        movie.genres?.let { genres ->
            binding.movieTypes.text = getFormattedMovieGenres(genres)
        } ?: run {
            binding.pointSeparator.visibility = View.GONE
        }

        movie.runtime?.let { movieTime ->
            val hours = movieTime / 60
            binding.movieDuration.text = "${hours}h${movieTime - (hours*60)}"
        } ?: run {
            binding.pointSeparator.visibility = View.GONE
        }


        actorAdapter = ActorsAdapter()
        binding.actorsRecyclerView.adapter = actorAdapter
        binding.actorsRecyclerView.setHasFixedSize(true)
        binding.actorsRecyclerView.addItemDecoration(ActorMarginItemDecoration(requireContext(), actorAdapter))
        actorAdapter.setItems(movie.cast)

    }

    private fun setObservers() {
        detailsViewModel.movieDetails.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSuccess -> {
                    hideLoader()
                    this.movie = it.movie
                    setMovieData()
                }

                is MovieDetailsDataError -> {
                    hideLoader()
                    Log.e("DATA ERROR", it.ex.message)
                }

                is MovieDetailsError -> {
                    hideLoader()
                    Log.e("ERROR", it.ex.message!!)
                }

                is MovieDetailsLoading -> {
                    showLoader()
                }
            }
        }
    }

    private fun setUIListeners() {
        binding.rateButton.setOnClickListener {  }

        binding.movieTrailerButton.setOnClickListener {
            movie.trailerKey?.let { key ->
                val action = DetailsFragmentDirections.trailerFragmentAction(key)
                findNavController().navigate(action)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.no_trailer),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupMenu() {
        val menuResId : Int = R.menu.movie_details_menu

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi")
            override fun onPrepareMenu(menu: Menu) {
                if (menu is MenuBuilder) {
                    menu.setOptionalIconsVisible(true)
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuResId, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_playlist -> {
                        detailsViewModel.init(authData.access_token, authData.account_id)
                        detailsViewModel.getLists.observe(viewLifecycleOwner) {
                            when (it) {
                                is GetListsSuccess -> {
                                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
                                    val playlists = it.lists.map { e -> PlaylistItem(e.id, drawable!!, e.name, e.number_of_items.toString() + " film(s)") }
                                        .toMutableList()
                                    playlistList[0] = ListPlaylistItem(playlists)
                                    showPlaylistDialog()
                                }

                                is GetListsDataError -> {
                                    Log.e("DATA ERROR", it.ex.message)
                                }

                                is GetListsError -> {
                                    Log.e("ERROR", it.ex.message!!)
                                }

                                else -> {
                                }
                            }
                        }
                    }
                    R.id.action_like -> {
                        // Gérer l'action de "like" ici
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun showPlaylistDialog() {
        val playlistNames = playlistList[0].playlists.map { it.name }.toTypedArray()

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Playlists")
        alertDialogBuilder.setItems(playlistNames) { _, position ->
            val playlistId = playlistList[0].playlists[position].id
            //AJOUTER LE FILM A LA PLAYLIST

            val item = Item("movie", movie.id)
            val listItems = ListItems(items = listOf(item))

            detailsViewModel.addMovie(authData.access_token!!, playlistId, listItems)
            Log.d("log", "playlist id dans ajout : $playlistId")
        }
        alertDialogBuilder.setNegativeButton("Annuler", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun getFormattedMovieGenres(genres: List<Genre>): String {
        var formattedGenres = ""
        genres.forEachIndexed { index, genre ->
            if (index > 3) {
                formattedGenres += "..."
                return@forEachIndexed
            }
            formattedGenres += if (index == 0) genre.name else ", ${genre.name}"
        }
        return formattedGenres
    }

    class ActorMarginItemDecoration(val context: Context, private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val itemPosition = parent.getChildAdapterPosition(view)
            if(itemPosition == 0){
                outRect.left = context.resources.getDimensionPixelSize(R.dimen.details_horizontal_margin)
            }else if(itemPosition == (adapter.itemCount - 1)){
                outRect.right = context.resources.getDimensionPixelSize(R.dimen.details_horizontal_margin)
            }

        }
    }
}
