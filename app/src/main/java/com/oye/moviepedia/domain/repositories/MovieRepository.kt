package com.oye.moviepedia.domain.repositories

import com.oye.moviepedia.data.repositories.RemoteMovieRepository
import com.oye.moviepedia.domain.entities.Movie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

interface MovieRepository {

    fun getUpcomingMovies(): List<Movie>

    fun getNewMovies(): List<Movie>

    fun getNowPlayingMovies(): List<Movie>

    fun getPopularMovies(): List<Movie>

    fun getMovieDetails(id: Int): Movie?

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(repository: RemoteMovieRepository): MovieRepository
}