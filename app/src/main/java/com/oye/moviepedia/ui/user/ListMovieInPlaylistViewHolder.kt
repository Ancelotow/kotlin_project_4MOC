package com.oye.moviepedia.ui.user

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R

class ListMovieInPlaylistViewHolder(
    v: View,
    private val movieListener: MovieInPlaylistListAdapter.MovieListener,
    private val movieLongListener: MovieInPlaylistListAdapter.MovieLongListener,
    private val spanCount: Int
) : RecyclerView.ViewHolder(v) {

    private val movies = v.findViewById<RecyclerView>(R.id.recycler_movies)

    fun setItem(item: ListMovieItem) {
        val gridLayoutManager = GridLayoutManager(itemView.context, spanCount)
        movies.layoutManager = gridLayoutManager
        movies.adapter = MovieInPlaylistListAdapter(item.movies, null, movieListener, movieLongListener)
    }
}


class ListMovieInPlaylistListAdapter(
    val items: MutableList<ListMovieItem>,
    val activity: Activity?,
    private val movieListener: MovieInPlaylistListAdapter.MovieListener,
    private val movieLongListener: MovieInPlaylistListAdapter.MovieLongListener,
) : RecyclerView.Adapter<ListMovieInPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMovieInPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list_movie, parent, false)
        val item = items[viewType]
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        layoutParams.width = parent.measuredWidth
        view.layoutParams = layoutParams

        return ListMovieInPlaylistViewHolder(view, movieListener, movieLongListener, item.spanCount)
    }


    override fun onBindViewHolder(holder: ListMovieInPlaylistViewHolder, position: Int) {
        holder.setItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

data class ListMovieItem(
    val movies: MutableList<MovieItem>,
    val spanCount: Int = 3
)
