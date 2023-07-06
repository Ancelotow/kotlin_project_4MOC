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
        val response = service.logout(access_token).execute()
        if(response.isSuccessful) {
            return response.body()?.success!!
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }
}