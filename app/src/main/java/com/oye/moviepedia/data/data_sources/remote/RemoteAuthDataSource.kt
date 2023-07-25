package com.oye.moviepedia.data.data_sources.remote

import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.dto.TokenDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteAuthDataSource @Inject constructor(
    private val service: ApiService
) : AuthDataSource {
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