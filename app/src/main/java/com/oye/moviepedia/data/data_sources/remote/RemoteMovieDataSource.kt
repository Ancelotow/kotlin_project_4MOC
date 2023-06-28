package com.oye.moviepedia.data.data_sources.remote

import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import com.oye.moviepedia.data.services.RetrofitSingletonService
import com.oye.moviepedia.domain.entities.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteMovieDataSource @Inject constructor() : MovieDataSource {
    private val service: ApiService

    init {
        service = RetrofitSingletonService.getInstance().service
    }

    override fun fetchMovies(
        primaryDateRelease: LocalDate?,
        sortBy: String?,
        includeAdult: Boolean
    ): List<MovieDto> {
        var dateStr: String? = null
        if(primaryDateRelease != null) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            dateStr = primaryDateRelease.format(formatter)
        }
        val response = service.getMovies(dateStr, sortBy, false).execute()
        if(response.isSuccessful) {
            return response.body()!!.results
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun fetchNowPlayingMovies(): List<MovieDto> {
        val response = service.getNowPlayingMovies().execute()
        if(response.isSuccessful) {
            return response.body()!!.results
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun fetchCreditsMovies(id: Int): CreditsResultDto {
        val response = service.getCreditMovie(id).execute()
        if(response.isSuccessful) {
            return response.body()!!
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun fetchPopularMovies(): List<MovieDto> {
        val response = service.getPopularMovies().execute()
        if(response.isSuccessful) {
            return response.body()!!.results
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun getMovieDetails(id: Int): MovieDto? {
        val response = service.getMovie(id).execute()
        if(response.isSuccessful) {
            return response.body()
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

}