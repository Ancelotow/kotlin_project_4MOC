package com.oye.moviepedia.ui.user

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R
import com.oye.moviepedia.ui.home.MovieListAdapter

class ListMovieInPlaylistViewHolder(
    v: View,
    private val movieListener: MovieInPlaylistListAdapter.MovieListener,
    private val spanCount: Int
) : RecyclerView.ViewHolder(v) {

    private val title = v.findViewById<TextView>(R.id.text_list_title)
    private val movies = v.findViewById<RecyclerView>(R.id.recycler_movies)

    fun setItem(item: ListMovieItem) {
        title.text = item.title
        val gridLayoutManager = GridLayoutManager(itemView.context, spanCount)
        movies.layoutManager = gridLayoutManager
        movies.adapter = MovieInPlaylistListAdapter(item.movies, null, movieListener)
    }
}


class ListMovieInPlaylistListAdapter(
    val items: MutableList<ListMovieItem>,
    val activity: Activity?,
    private val movieListener: MovieInPlaylistListAdapter.MovieListener,
) : RecyclerView.Adapter<ListMovieInPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieInPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list_movie, parent, false)
        val item = items[viewType]
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        layoutParams.width = parent.measuredWidth
        view.layoutParams = layoutParams

        return ListMovieInPlaylistViewHolder(view, movieListener, item.spanCount)
    }


    override fun onBindViewHolder(holder: ListMovieInPlaylistViewHolder, position: Int) {
        holder.setItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

data class ListMovieItem(
    val title: String,
    val movies: MutableList<MovieItem>,
    val spanCount: Int = 3
)
