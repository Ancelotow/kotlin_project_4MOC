package com.oye.moviepedia.data.data_sources.remote

import android.util.Log
import com.oye.moviepedia.data.data_sources.PlaylistDataSource
import com.oye.moviepedia.data.dto.DetailPlaylistDto
import com.oye.moviepedia.data.dto.PlaylistDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemotePlaylistDataSource @Inject constructor() : PlaylistDataSource {
    private val service: ApiService = RetrofitSingletonService.getInstance().service

    override fun createList(token: String, name: String): Int {
        val response = service.createList("Bearer $token", name, "fr").execute()
        if(response.isSuccessful) {
            return response.body()?.id ?: 0
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun getLists(token: String, accountId: String): List<PlaylistDto> {
        val response = service.getLists("Bearer $token", accountId).execute()
        if(response.isSuccessful) {
            return response.body()!!.results
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }


    override fun getListDetail(token: String, listId: Int): DetailPlaylistDto {
        val response = service.getPlaylistDetail("Bearer $token", listId).execute()
        if(response.isSuccessful) {
            return response.body()!!
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun deletePlaylist(token: String, listId: Int): Boolean {
        val response = service.deletePlaylist("Bearer $token", listId).execute()
        if(response.isSuccessful) {
            Log.d("log", "dans successful remote data source : ${response.body()!!}")
            return response.body()!!.success
        } else {
            Log.d("log", "dans error remote data source $response")
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }
}