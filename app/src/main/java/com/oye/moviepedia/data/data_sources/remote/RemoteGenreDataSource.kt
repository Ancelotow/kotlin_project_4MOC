package com.oye.moviepedia.data.data_sources.remote

import com.oye.moviepedia.data.data_sources.GenreDataSource
import com.oye.moviepedia.data.dto.GenreDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import javax.inject.Inject

class RemoteGenreDataSource @Inject constructor() : GenreDataSource {
    private val service: ApiService = RetrofitSingletonService.getInstance().service

    override fun fetchMovieGenres(): List<GenreDto> {
        val response = service.getMovieGenres().execute()
        if(response.isSuccessful) {
            return response.body()!!.genres
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun fetchTvGenres(): List<GenreDto> {
        val response = service.getTvGenres().execute()
        if(response.isSuccessful) {
            return response.body()!!.genres
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }
}