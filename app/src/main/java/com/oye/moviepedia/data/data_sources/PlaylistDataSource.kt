package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.data.dto.PlaylistDto
import com.oye.moviepedia.domain.entities.Item
import com.oye.moviepedia.domain.entities.ListItems

interface PlaylistDataSource {

    fun createList(token: String, name: String): Int
    fun getLists(token: String, accountId: String): List<PlaylistDto>
    fun getListDetail(token: String, listId: Int): DetailPlaylistDto
    fun deletePlaylist(token: String, listId: Int): Boolean
    fun addMovie(token: String, listId: Int, newItem: ListItems): Boolean
    fun removeMovie(token: String, listId: Int, item: ListItems): Boolean

    }