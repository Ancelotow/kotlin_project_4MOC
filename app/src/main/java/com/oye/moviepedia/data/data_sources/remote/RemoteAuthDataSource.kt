package com.oye.moviepedia.data.data_sources.remote

import android.util.Log
import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.dto.TokenDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteAuthDataSource @Inject constructor() : AuthDataSource {
    private val service: ApiService = RetrofitSingletonService.getInstance().service

    override fun getRequestToken(): TokenDto {
        val response = service.getRequestToken().execute()
        if(response.isSuccessful) {
            return response.body()!!
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun getAccountId(requestToken: String): AuthDto {
        val response = service.getAccountId(requestToken).execute()
        if(response.isSuccessful) {
            return response.body()!!
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

}