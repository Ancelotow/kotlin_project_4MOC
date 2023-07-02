package com.oye.moviepedia.config

import com.oye.moviepedia.data.repositories.RemoteMovieRepository
import com.oye.moviepedia.domain.repositories.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(repository: RemoteMovieRepository): MovieRepository
}