package com.oye.moviepedia.data.repositories

import android.util.Log
import com.oye.moviepedia.data.data_sources.PlaylistDataSource
import com.oye.moviepedia.domain.repositories.PlaylistRepository
import javax.inject.Inject

class RemotePlaylistRepository @Inject constructor(
    private val dataSource: PlaylistDataSource
) : PlaylistRepository {

    override fun createList(token: String, name: String): Boolean {
        val success = dataSource.createList(token, name)
        return success;
    }

}