package com.oye.moviepedia.config

import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteAuthDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteMovieDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun provideMovieDataSource(dataSource: RemoteMovieDataSource): MovieDataSource

    @Binds
    @Singleton
    abstract fun provideAuthDataSource(dataSource: RemoteAuthDataSource): AuthDataSource

}