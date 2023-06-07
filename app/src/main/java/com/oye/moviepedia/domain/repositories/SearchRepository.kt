package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.repositories.RemoteSearchRepository
import com.oye.moviepedia.domain.entities.SearchResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface SearchRepository {

    fun getSearchResult(query: String): List<SearchResult>

}

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideSearcgRepository(repository: RemoteSearchRepository): SearchRepository
}