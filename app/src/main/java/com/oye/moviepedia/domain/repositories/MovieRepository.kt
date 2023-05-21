package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.repositories.RemoteMovieRepository
import com.oye.moviepedia.domain.entities.Movie
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface MovieRepository {

    fun getUpcomingMovies(): List<Movie>

    fun getNewMovies(): List<Movie>

    fun getNowPlayingMovies(): List<Movie>

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(repository: RemoteMovieRepository): MovieRepository
}