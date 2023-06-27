package com.oye.moviepedia.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.oye.moviepedia.databinding.FragmentDetailsBinding
import com.oye.moviepedia.ui.home.MovieItem
import com.squareup.picasso.Picasso

class DetailsFragment: Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var movieItem: MovieItem
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
        setupUI()
        setObservers()
        setUIListeners()
    }

    private fun setupUI() {
        movieItem = args.movie
        binding.movieTitle.text = movieItem.name
        binding.movieDescription.text = movieItem.description
        Picasso.get().load(movieItem.urlPoster).into(binding.moviePicture)
    }

    private fun setObservers() {

    }

    private fun setUIListeners() {

    }
}