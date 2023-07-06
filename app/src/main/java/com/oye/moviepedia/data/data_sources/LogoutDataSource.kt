package com.oye.moviepedia.data.data_sources

interface LogoutDataSource {

    fun logout(access_token: String): Boolean
}