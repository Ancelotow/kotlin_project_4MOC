package com.oye.moviepedia.ui.home

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentHomeBinding
import com.oye.moviepedia.domain.uses_cases.NewMovieError
import com.oye.moviepedia.domain.uses_cases.NewMovieLoading
import com.oye.moviepedia.domain.uses_cases.NewMovieSuccess
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieError
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieLoading
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieSuccess
import com.oye.moviepedia.domain.uses_cases.PopularMovieError
import com.oye.moviepedia.domain.uses_cases.PopularMovieLoading
import com.oye.moviepedia.domain.uses_cases.PopularMovieSuccess
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieError
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieLoading
import com.oye.moviepedia.domain.uses_cases.UpcomingMovieSuccess
import com.oye.moviepedia.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment : BaseFragment(), MovieListAdapter.MovieListener {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val movieList = ArrayList<ListMovieItem>(4).apply {
        repeat(4) {
            add(ListMovieItem("", mutableListOf()))
        }
    }

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
            0f, 0f, width, 0f, Color.parseColor("#FF9CCCA5"), Color.parseColor("#FF51B1DF"),
            Shader.TileMode.CLAMP
        )

        val recyclerView = binding.recyclerNewMovies
        val linearLayoutManager = LinearLayoutManager(container?.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        movieList.ensureCapacity(4)
        viewModel.onEventChanged(HomeEvent.OnNewMovies)
        viewModel.onEventChanged(HomeEvent.OnNowPlayingMovies)
        viewModel.onEventChanged(HomeEvent.OnUpcomingMovies)
        viewModel.onEventChanged(HomeEvent.OnPopularMovies)

        initNewMovies()
        initNowPlayingMovies()
        initUpcomingMovies()
        initPopularMovies()
        return root
    }

    private fun initNewMovies() {
        viewModel.newMoviesState.observe(viewLifecycleOwner) {
            when (it) {
                is NewMovieSuccess -> {
                    val newMovies =
                        it.movies.map { e -> MovieItem(e.id,e.title, e.posterUrl, e.director) }
                            .toMutableList()
                    movieList[0] = ListMovieItem(getString(R.string.home_new_movies), newMovies)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is NewMovieLoading -> {
                    movieList[0] = ListMovieItem(getString(R.string.home_new_movies), mutableListOf(), true)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is NewMovieError -> {
                    movieList[0] = ListMovieItem(getString(R.string.home_new_movies), mutableListOf(), errorMessage = it.ex.message)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }
            }
        }
    }

    private fun initNowPlayingMovies() {
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is NowPlayingMovieSuccess -> {
                    val movies = it.movies.map { e -> MovieItem(e.id,e.title, e.posterUrl, e.director) }
                        .toMutableList()
                    movieList[1] = ListMovieItem(getString(R.string.home_movies_showing), movies)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is NowPlayingMovieLoading -> {
                    movieList[1] = ListMovieItem(getString(R.string.home_movies_showing), mutableListOf(), true)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is NowPlayingMovieError -> {
                    movieList[1] = ListMovieItem(getString(R.string.home_movies_showing), mutableListOf(), errorMessage = it.ex.message)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }
            }
        }
    }

    private fun initUpcomingMovies() {
        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingMovieSuccess -> {
                    val formatter = DateTimeFormatter.ofPattern(getString(R.string.date_format))
                    val movies = it.movies.map { e ->
                        MovieItem(
                            e.id,
                            e.title,
                            e.posterUrl,
                            e.releaseDate.format(formatter)
                        )
                    }.toMutableList()
                    movieList[2] = ListMovieItem(getString(R.string.home_movies_upcoming), movies)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is UpcomingMovieLoading -> {
                    movieList[2] = ListMovieItem(getString(R.string.home_movies_upcoming), mutableListOf(), true)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is UpcomingMovieError -> {
                    movieList[2] = ListMovieItem(getString(R.string.home_movies_upcoming), mutableListOf(), errorMessage = it.ex.message)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }
            }
        }
    }

    private fun initPopularMovies() {
        viewModel.popularMovies.observe(viewLifecycleOwner) {
            when (it) {
                is PopularMovieSuccess -> {
                    val movies = it.movies.map { e ->
                        MovieItem(
                            e.id,
                            e.title,
                            e.posterUrl,
                            e.director
                        )
                    }.toMutableList()
                    movieList[3] = ListMovieItem(getString(R.string.home_movies_popular), movies)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is PopularMovieLoading -> {
                    movieList[3] = ListMovieItem(getString(R.string.home_movies_popular), mutableListOf(), true)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }

                is PopularMovieError -> {
                    movieList[3] = ListMovieItem(getString(R.string.home_movies_popular), mutableListOf(), errorMessage = it.ex.message)
                    binding.recyclerNewMovies.adapter = ListMovieListAdapter(movieList, activity, this)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieCLick(movieId: Int) {
        val action = HomeFragmentDirections.detailsFragmentAction(movieId)
        findNavController().navigate(action)
    }
}

