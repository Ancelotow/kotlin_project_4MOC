package com.oye.moviepedia.ui.user

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oye.moviepedia.R
import com.oye.moviepedia.databinding.FragmentUserBinding
import com.oye.moviepedia.domain.uses_cases.LikedMovieDataError
import com.oye.moviepedia.domain.uses_cases.LikedMovieError
import com.oye.moviepedia.domain.uses_cases.LikedMovieSuccess
import com.oye.moviepedia.domain.uses_cases.NewMovieDataError
import com.oye.moviepedia.domain.uses_cases.NewMovieError
import com.oye.moviepedia.domain.uses_cases.NewMovieSuccess
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieDataError
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieError
import com.oye.moviepedia.domain.uses_cases.NowPlayingMovieSuccess
import com.oye.moviepedia.ui.home.HomeViewModel
import com.oye.moviepedia.ui.home.ListMovieItem
import com.oye.moviepedia.ui.home.ListMovieListAdapter
import com.oye.moviepedia.ui.home.MovieItem
import com.oye.moviepedia.ui.home.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment(), MovieListAdapter.MovieListener {

    private val viewModel: UserViewModel by viewModels()
    private var _binding: FragmentUserBinding? = null

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
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        movieList.ensureCapacity(4)
        initLikedMovies()
        val buttonAddPlaylist = binding.addButtonPlaylist
        buttonAddPlaylist.setOnClickListener {
            onAddButtonClick()
        }

        return root
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

    private fun onAddButtonClick() {
        val editText = binding.editTextPlaylist
        if (!editText.isVisible) {
            editText.isVisible = true
        } else {
            editText.isVisible = false
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

}