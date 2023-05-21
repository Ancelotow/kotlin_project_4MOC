package com.oye.moviepedia.data.services

import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.ListMovieResultDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("movie/now_playing?language=fr-FR&page=1")
    fun getNowPlayingMovies(): Call<ListMovieResultDto>

    @GET("movie/{id}/credits")
    fun getCreditMovie(@Path("id") id: Int): Call<CreditsResultDto>

}