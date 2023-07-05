package com.oye.moviepedia.data.data_sources.remote

import android.util.Log
import com.oye.moviepedia.data.data_sources.LogoutDataSource
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteLogoutDataSource @Inject constructor() : LogoutDataSource {
    private val service: ApiService = RetrofitSingletonService.getInstance().service

    override fun logout(access_token: String): Boolean {
        Log.d("log", "dans remote data source : $access_token")
        val response = service.logout(access_token).execute()
        Log.d("log", "response avant check : $response")
        if(response.isSuccessful) {
            Log.d("log", "logout : ${response.body()}")
            return response.body()?.success!!
        } else {
            Log.d("log", "logout dans ELSE : $response")
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }
}