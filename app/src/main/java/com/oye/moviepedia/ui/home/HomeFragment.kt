package com.oye.moviepedia.ui.home

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentHomeBinding
import com.oye.moviepedia.domain.uses_cases.NewMovieDataError
import com.oye.moviepedia.domain.uses_cases.NewMovieError
import com.oye.moviepedia.domain.uses_cases.NewMovieSuccess
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val title = binding.appTitle
        val paint = title.paint
        val width = paint.measureText(title.text.toString())
        title.paint.shader = LinearGradient(
            0f, 0f, width, 0f, Color.parseColor("#9CCCA5"), Color.parseColor("#51B1DF"),
            Shader.TileMode.CLAMP
        )

        val recyclerView = binding.recyclerNewMovies
        val linearLayoutManager = LinearLayoutManager(container?.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        var moviesLists = mutableListOf(
            ListMovieItem(getString(R.string.home_new_movies), mutableListOf()),
            ListMovieItem(getString(R.string.home_movies_showing), mutableListOf()),
            ListMovieItem(getString(R.string.home_movies_coming), mutableListOf()),
            ListMovieItem(getString(R.string.home_movies_popular), mutableListOf()),
        )

        viewModel.newMoviesState.observe(viewLifecycleOwner) {
            when (it) {
                is NewMovieSuccess -> {
                    val newMovies = it.movies.map { e -> MovieItem(e.title, e.posterUrl, e.director) }.toMutableList()
                    Log.d("DATA", newMovies.size.toString())
                    moviesLists[0] = ListMovieItem(getString(R.string.home_new_movies), newMovies)
                    recyclerView.adapter = ListMovieListAdapter(moviesLists, activity)
                }

                is NewMovieDataError -> {
                    Log.e("DATA ERROR", it.ex.message)
                }

                is NewMovieError -> {
                    Log.e("ERROR", it.ex.message!!)
                }

                else -> {
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

