package com.oye.moviepedia.data.services

import com.oye.moviepedia.data.dto.AccountDetailDto
import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.GenreResultDto
import com.oye.moviepedia.data.dto.ListMovieResultDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.MovieTrailerDto
import com.oye.moviepedia.data.dto.ListSearchResultDto
import com.oye.moviepedia.data.dto.SessionDto
import com.oye.moviepedia.data.dto.TokenDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field

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

    @GET("movie/{id}?language=fr-FR")
    fun getMovie(@Path("id") id: Int): Call<MovieDto>

    @GET("movie/{id}/videos?language=fr-FR")
    fun getMovieTrailers(@Path("id") id: Int): Call<MovieTrailerDto>

    @GET("authentication/token/new")
    fun getRequestToken(): Call<TokenDto>

    @POST("authentication/session/new")
    @FormUrlEncoded
    fun createSession(
        @Field("request_token") requestToken: String,
    ): Call<SessionDto>

    @POST("account/{account_id}")
    @FormUrlEncoded
    fun getAccountDetails(
        @Field("session_id") sessionId: String,
        @Path("account_id") id: Int,
    ): Call<AccountDetailDto>

}