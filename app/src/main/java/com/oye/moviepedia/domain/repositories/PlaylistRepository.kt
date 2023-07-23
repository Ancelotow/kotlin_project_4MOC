package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.domain.entities.NewItem
import com.oye.moviepedia.domain.entities.Playlist

interface PlaylistRepository {

    fun createList(token: String, name: String): Int
    fun getLists(token: String, accountId: String): List<Playlist>
    fun getListDetail(token: String, listId: Int): DetailPlaylistDto
    fun deletePlaylist(token: String, listId: Int): Boolean
    fun addMovie(token: String, listId: Int, newItem: List<NewItem>): Boolean
    fun removeMovie(token: String, listId: Int, newItem: List<NewItem>): Boolean

    }