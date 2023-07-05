package com.oye.moviepedia.domain.repositories

interface LogoutRepository {

    fun logout(access_token: String): Boolean
}