package com.oye.moviepedia.data.data_sources.remote

import android.util.Log
import com.oye.moviepedia.data.data_sources.PlaylistDataSource
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemotePlaylistDataSource @Inject constructor() : PlaylistDataSource {
    private val service: ApiService = RetrofitSingletonService.getInstance().service

    override fun createList(token: String, name: String): Boolean {
        Log.d("log", "dans remote data source token : $token")
        val response = service.createList("Bearer $token", name, "fr").execute()
        if(response.isSuccessful) {
            Log.d("log", "dans successful remote data source")
            return response.body()?.success ?: false
        } else {
            Log.d("log", "dans error remote data source $response")
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }


}