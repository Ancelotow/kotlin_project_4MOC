package com.oye.moviepedia.domain.repositories

interface PlaylistRepository {

    fun createList(token: String, name: String): Boolean
}