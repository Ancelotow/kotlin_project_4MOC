package com.oye.moviepedia.data.repositories

import android.util.Log
import com.oye.moviepedia.data.data_sources.PlaylistDataSource
import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.data.dto.PlaylistDto
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.NewItem
import com.oye.moviepedia.domain.entities.Playlist
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import javax.inject.Inject

class RemotePlaylistRepository @Inject constructor(
    private val dataSource: PlaylistDataSource
) : PlaylistRepository {

    override fun createList(token: String, name: String): Int {
        val success = dataSource.createList(token, name)
        return success;
    }

    override fun getLists(token: String, accountId: String): List<Playlist> {
        val playlistDto = dataSource.getLists(token, accountId)
        val playlists = ArrayList<Playlist>()
        for (playlist in playlistDto) {
            Log.d("log", "playlist : ${Playlist.fromPlaylistDto(playlist)}")
            playlists.add(Playlist.fromPlaylistDto(playlist))
        }
        return playlists;
    }

    override fun getListDetail(token: String, listId: Int): DetailPlaylistDto {
        val detail = dataSource.getListDetail(token, listId)
        return detail;
    }

    override fun deletePlaylist(token: String, listId: Int): Boolean {
        val success = dataSource.deletePlaylist(token, listId)
        return success;
    }

    override fun addMovie(token: String, listId: Int, newItem: List<NewItem>): Boolean {
        val success = dataSource.addMovie(token, listId, newItem)
        return success;
    }

    override fun removeMovie(token: String, listId: Int, newItem: List<NewItem>): Boolean {
        val success = dataSource.removeMovie(token, listId, newItem)
        return success;
    }

}