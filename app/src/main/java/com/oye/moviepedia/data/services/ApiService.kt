package com.oye.moviepedia.data.services

import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.GenreResultDto
import com.oye.moviepedia.data.dto.ListMovieResultDto
import com.oye.moviepedia.data.dto.ListSearchResultDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.util.Date

interface ApiService {

    @GET("discover/movie?language=fr-FR&page=1")
    fun getMovies(
        @Query("primary_release_date.gte") primaryReleaseDate: String?,
        @Query("sort_by") sortBy: String?,
        @Query("include_adult") includeAdult: Boolean = false
    ): Call<ListMovieResultDto>

    @GET("movie/now_playing?language=fr-FR&page=1")
    fun getNowPlayingMovies(): Call<ListMovieResultDto>

    @GET("movie/{id}/credits")
    fun getCreditMovie(@Path("id") id: Int): Call<CreditsResultDto>

    @GET("movie/popular?language=fr-FR&page=1")
    fun getPopularMovies(): Call<ListMovieResultDto>

    @GET("search/multi?language=fr-FR&page=1")
    fun searchMovies(@Query("query") query: String): Call<ListSearchResultDto>

    @GET("genre/movie/list?language=fr-FR")
    fun getMovieGenres(): Call<GenreResultDto>

    @GET("genre/tv/list?language=fr-FR")
    fun getTvGenres(): Call<GenreResultDto>

}