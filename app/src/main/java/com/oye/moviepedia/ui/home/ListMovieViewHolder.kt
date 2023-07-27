package com.oye.moviepedia.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.res.stringResource
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R

class ListMovieViewHolder(
    v: View,
    private val movieListener: MovieListAdapter.MovieListener
) : RecyclerView.ViewHolder(v) {

    private val title = v.findViewById<TextView>(R.id.text_list_title)
    private val movies = v.findViewById<RecyclerView>(R.id.recycler_movies)
    private val loader = v.findViewById<View>(R.id.loader)
    private val error = v.findViewById<TextView>(R.id.txtError)
    private val emptyListText = v.findViewById<TextView>(R.id.textEmptyList)

    fun setItem(item: ListMovieItem) {
        title.text = item.title
        if(item.movies.isEmpty()) {
            movies.visibility = View.GONE
            emptyListText.visibility = View.VISIBLE
            emptyListText.text = itemView.context.getString(R.string.empty_movie_list)
        } else {
            movies.visibility = View.VISIBLE
            emptyListText.visibility = View.GONE
            val linearLayoutManager = LinearLayoutManager(itemView.context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            movies.layoutManager = linearLayoutManager
            movies.adapter = MovieListAdapter(item.movies, null, movieListener)
        }

        if(item.isLoading) {
            loader.visibility = View.VISIBLE
            emptyListText.visibility = View.GONE

        } else {
            loader.visibility = View.GONE
        }

        if(item.errorMessage != null) {
            error.visibility = View.VISIBLE
            error.text = item.errorMessage
        } else {
            error.visibility = View.GONE
        }
    }

}

class ListMovieListAdapter(
    val items: MutableList<ListMovieItem>,
    val activity: Activity?,
    private val movieListener: MovieListAdapter.MovieListener
) : RecyclerView.Adapter<ListMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list_movie, parent, false)
        return ListMovieViewHolder(view, movieListener)
    }

    override fun onBindViewHolder(holder: ListMovieViewHolder, position: Int) {
        holder.setItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

data class ListMovieItem(
    val title: String,
    val movies: MutableList<MovieItem>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)