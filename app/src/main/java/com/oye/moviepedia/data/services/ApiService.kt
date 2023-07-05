package com.oye.moviepedia.data.services

import com.oye.moviepedia.data.dto.AuthDto
import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.GenreResultDto
import com.oye.moviepedia.data.dto.ListMovieResultDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.MovieTrailerDto
import com.oye.moviepedia.data.dto.ListSearchResultDto
import com.oye.moviepedia.data.dto.TokenDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field

interface ApiService {

    @GET("3/discover/movie?language=fr-FR&page=1")
    fun getMovies(
        @Query("primary_release_date.gte") primaryReleaseDate: String?,
        @Query("sort_by") sortBy: String?,
        @Query("include_adult") includeAdult: Boolean = false
    ): Call<ListMovieResultDto>

    @GET("3/movie/now_playing?language=fr-FR&page=1")
    fun getNowPlayingMovies(): Call<ListMovieResultDto>

    @GET("3/movie/{id}/credits")
    fun getCreditMovie(@Path("id") id: Int): Call<CreditsResultDto>

    @GET("3/movie/popular?language=fr-FR&page=1")
    fun getPopularMovies(): Call<ListMovieResultDto>

    @GET("3/search/multi?language=fr-FR&page=1")
    fun searchMovies(@Query("query") query: String): Call<ListSearchResultDto>

    @GET("3/genre/movie/list?language=fr-FR")
    fun getMovieGenres(): Call<GenreResultDto>

    @GET("3/genre/tv/list?language=fr-FR")
    fun getTvGenres(): Call<GenreResultDto>

    @GET("3/movie/{id}?language=fr-FR")
    fun getMovie(@Path("id") id: Int): Call<MovieDto>

    @GET("3/movie/{id}/videos?language=fr-FR")
    fun getMovieTrailers(@Path("id") id: Int): Call<MovieTrailerDto>

    @POST("4/auth/request_token")
    fun getRequestToken(): Call<TokenDto>

    @POST("4/auth/access_token")
    @FormUrlEncoded
    fun getAccountId(
        @Field("request_token") requestToken: String,
    ): Call<AuthDto>

}