package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems
import com.oye.moviepedia.domain.entities.Playlist

interface PlaylistRepository {

    fun createList(token: String, name: String): Int
    fun getLists(token: String, accountId: String): List<Playlist>
    fun getListDetail(token: String, listId: Int): DetailPlaylistDto
    fun deletePlaylist(token: String, listId: Int): Boolean
    fun addMovie(token: String, listId: Int, newItem: ListItems): Boolean
    fun removeMovie(token: String, listId: Int, item: ListItems): Boolean

    }