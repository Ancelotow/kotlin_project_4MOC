package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.data.dto.PlaylistDto

interface PlaylistDataSource {

    fun createList(token: String, name: String): Int
    fun getLists(token: String, accountId: String): List<PlaylistDto>

    fun getListDetail(token: String, listId: Int): DetailPlaylistDto


    }