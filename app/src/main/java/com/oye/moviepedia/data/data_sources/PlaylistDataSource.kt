package com.oye.moviepedia.data.data_sources

interface PlaylistDataSource {

    fun createList(token: String, name: String): Boolean

}