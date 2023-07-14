package com.oye.moviepedia.data.data_sources.remote

import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.GenreDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.MovieTrailerDto
import com.oye.moviepedia.data.dto.SearchDto
import com.oye.moviepedia.data.exceptions.RemoteException
import com.oye.moviepedia.data.services.ApiService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteMovieDataSource @Inject constructor(
    private val service: ApiService
) : MovieDataSource {

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

    override fun getMovieTrailers(id: Int): MovieTrailerDto? {
        val response = service.getMovieTrailers(id).execute()
        if(response.isSuccessful) {
            return response.body()
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

    override fun fetchSearchResult(query: String): List<SearchDto> {
        val response = service.searchMovies(query).execute()
        if(response.isSuccessful) {
            return response.body()!!.results
        } else {
            throw RemoteException(response.code(), response.errorBody().toString())
        }
    }

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