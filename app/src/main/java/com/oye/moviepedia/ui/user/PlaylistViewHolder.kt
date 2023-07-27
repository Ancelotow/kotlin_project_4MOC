package com.oye.moviepedia.ui.user

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.R
import com.squareup.picasso.Picasso

class PlaylistViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val poster = v.findViewById<ImageView>(R.id.posterPlaylist)
    private val title = v.findViewById<TextView>(R.id.text_playlist_name)
    private val number_movies = v.findViewById<TextView>(R.id.text_number_of_movies)

    fun setItem(item: PlaylistItem) {
        poster.setImageDrawable(item.urlPoster)
        title.text = item.name
        number_movies.text = item.number_movies
    }

}

class PlaylistListAdapter(
    val playlists: MutableList<PlaylistItem>,
    val activity: Activity?,
    private val listener: PlaylistListener

) : RecyclerView.Adapter<PlaylistViewHolder>() {

    interface PlaylistListener {
        fun onPlaylistCLick(playlistId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view).listen { pos, type ->
            listener.onPlaylistCLick(playlists[pos].id)
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
    val id: Int,
    val urlPoster: Drawable,
    val name: String,
    val number_movies: String
)