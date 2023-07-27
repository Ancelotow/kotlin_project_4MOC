package com.oye.moviepedia.data.data_sources.firebase

import android.net.Uri
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import com.oye.moviepedia.data.data_sources.DynamicLinkDataSource
import com.oye.moviepedia.data.dto.MovieDto
import kotlinx.coroutines.tasks.await

class FirebaseDynamicLinkDataSource : DynamicLinkDataSource {
    override suspend fun createDynamicLinkForMovie(movie: MovieDto): String {
        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://oyemoviepedia.com?movieId=${movie.id}")
            domainUriPrefix = "https://oyemoviepedia.page.link"
            socialMetaTagParameters {
                title = movie.title
                description = movie.overview
                imageUrl = Uri.parse(movie.poster_path)
            }
            androidParameters("com.oye.moviepedia") {
                fallbackUrl = Uri.parse("https://oyemoviepedia.com/")
            }
        }

        return shortLinkTask.await().shortLink.toString()
    }
}