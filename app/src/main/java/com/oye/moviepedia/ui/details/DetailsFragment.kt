package com.oye.moviepedia.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.oye.moviepedia.data.dto.Genre
import com.oye.moviepedia.databinding.FragmentDetailsBinding
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.uses_cases.MovieDetailsDataError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsError
import com.oye.moviepedia.domain.uses_cases.MovieDetailsLoading
import com.oye.moviepedia.domain.uses_cases.MovieDetailsSuccess
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var movie: Movie
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
        detailsViewModel.getMovie(args.movieId)

        setObservers()
        setUIListeners()
    }

    private fun setupUI() {
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

    }

    private fun setObservers() {
        detailsViewModel.movieDetails.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailsSuccess -> {
                    this.movie = it.movie
                    setupUI()
                }

                is MovieDetailsDataError -> {
                    Log.e("DATA ERROR", it.ex.message)
                }

                is MovieDetailsError -> {
                    Log.e("ERROR", it.ex.message!!)
                }

                is MovieDetailsLoading -> {
                    //TODO : should show loader
                }
            }
        }
    }

    private fun setUIListeners() {
        binding.backButton.setOnClickListener {
        }

        binding.likeButton.setOnClickListener {  }
        binding.rateButton.setOnClickListener {  }
        binding.addToPlaylistButton.setOnClickListener {  }
        binding.movieTrailerButton.setOnClickListener {
            //https://www.youtube.com/watch?v={key}
        }
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
}