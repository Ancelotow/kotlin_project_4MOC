package com.oye.moviepedia.ui.home

import android.app.Activity
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R
import com.squareup.picasso.Picasso
import kotlinx.parcelize.Parcelize

class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val poster = v.findViewById<ImageView>(R.id.image_poster)
    private val title = v.findViewById<TextView>(R.id.text_title)
    private val description = v.findViewById<TextView>(R.id.text_description)

    fun setItem(item: MovieItem) {
        Picasso.get().load(item.urlPoster).into(poster);
        title.text = item.name
        description.text = item.description
    }

}

class MovieListAdapter(
    val movies: MutableList<MovieItem>,
    val activity: Activity?,
    private val listener: MovieListener
) : RecyclerView.Adapter<MovieViewHolder>() {

    interface MovieListener {
        fun onMovieCLick(movieId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view).listen { pos, type ->
            listener.onMovieCLick(movies[pos].id)
            print("Go to movie detail")
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setItem(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

}

@Parcelize
data class MovieItem(
    val id: Int,
    val name: String,
    val urlPoster: String,
    val description: String
): Parcelable