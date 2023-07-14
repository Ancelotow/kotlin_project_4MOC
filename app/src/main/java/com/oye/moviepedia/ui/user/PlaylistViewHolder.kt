package com.oye.moviepedia.ui.user

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R

class PlaylistViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val title = v.findViewById<TextView>(R.id.text_playlist_name)
    private val number_movies = v.findViewById<TextView>(R.id.text_number_of_movies)

    fun setItem(item: PlaylistItem) {
        title.text = item.name
        number_movies.text = item.number_movies
    }

}

class PlaylistListAdapter(
    val playlists: MutableList<PlaylistItem>,
    val activity: Activity?
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view).listen { pos, type ->
            // TODO: Aller vers le détail d'un film
            print("Go to playlist detail")
        }
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.setItem(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

}

data class PlaylistItem(
    val name: String,
    val number_movies: String
)