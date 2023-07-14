package com.oye.moviepedia.di

import com.oye.moviepedia.data.data_sources.AuthDataSource
import com.oye.moviepedia.data.data_sources.LogoutDataSource
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteAuthDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteLogoutDataSource
import com.oye.moviepedia.data.data_sources.remote.RemoteMovieDataSource
import com.oye.moviepedia.data.repositories.RemoteAuthRepository
import com.oye.moviepedia.data.repositories.RemoteLogoutRepository
import com.oye.moviepedia.data.repositories.RemoteMovieRepository
import com.oye.moviepedia.domain.repositories.AuthRepository
import com.oye.moviepedia.domain.repositories.LogoutRepository
import com.oye.moviepedia.domain.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieDataSource(): MovieDataSource = RemoteMovieDataSource()

    @Provides
    @Singleton
    fun provideAuthDataSource(): AuthDataSource = RemoteAuthDataSource()

    @Provides
    @Singleton
    fun provideLogoutDataSource(): LogoutDataSource = RemoteLogoutDataSource()

    @Provides
    @Singleton
    fun provideMovieRepository(dataSource: MovieDataSource): MovieRepository =
        RemoteMovieRepository(dataSource)

    @Provides
    @Singleton
    fun provideAuthRepository(dataSource: AuthDataSource): AuthRepository =
        RemoteAuthRepository(dataSource)

    @Provides
    @Singleton
    fun provideLogoutRepository(dataSource: LogoutDataSource): LogoutRepository =
        RemoteLogoutRepository(dataSource)
}