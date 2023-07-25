package com.oye.moviepedia.ui.details

import android.annotation.SuppressLint
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
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentDetailsBinding
import com.oye.moviepedia.domain.entities.Genre
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.uses_cases.MovieDetailsDataError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsLoading
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import com.oye.moviepedia.ui.BaseFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: BaseFragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var actorAdapter: ActorsAdapter
    private lateinit var movie: MovieDetails
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val args : DetailsFragmentArgs by navArgs()

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

    override fun onResume() {
        super.onResume()
        binding.toolbar.title = ""
    }

    private fun setupUI() {
        setupSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.setNavigationOnClickListener {
            onSupportNavigateUp()
        }
    }

    private fun setMovieData() {
        setupMenu()
        binding.detailsErrorLayout.visibility = View.GONE
        binding.movieTitle.text = movie.title
        binding.movieDescription.text = movie.description

        if (movie.posterUrl.isEmpty()) {
            binding.moviePicture.setImageResource(R.drawable.ic_movie)
        } else {
            Picasso.get().load(movie.posterUrl).into(binding.moviePicture)
        }

        val goodRatePercentage = (movie.noteAverage * 10).toInt()
        binding.rateValue.text = "${goodRatePercentage}%"
        binding.rateProgressIndicator.progress = goodRatePercentage

        if (goodRatePercentage < 50)
            binding.rateProgressIndicator.isSelected = true
        else if (goodRatePercentage < 75)
            binding.rateProgressIndicator.isActivated = true

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

    private fun setUpErrorUI() {
        binding.detailsContent.visibility = View.GONE
        binding.moviePicture.setImageResource(R.drawable.ic_movie)
        binding.moviePicture.scaleType = ImageView.ScaleType.CENTER_INSIDE
        binding.detailsErrorLayout.visibility = View.VISIBLE
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
                    setUpErrorUI()
                }

                is MovieDetailsError -> {
                    hideLoader()
                    Log.e("ERROR", it.ex.message.toString())
                    setUpErrorUI()
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
                // Handle for example visibility of menu items
                if (menu is MenuBuilder) {
                    menu.setOptionalIconsVisible(true)
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuResId, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.action_playlist-> {

                    }
                    R.id.action_like->{

                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
