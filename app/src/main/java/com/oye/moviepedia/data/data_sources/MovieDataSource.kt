package com.oye.moviepedia.data.data_sources

import com.oye.moviepedia.data.data_sources.remote.RemoteMovieDataSource
import com.oye.moviepedia.data.dto.CreditsResultDto
import com.oye.moviepedia.data.dto.MovieDto
import com.oye.moviepedia.data.dto.ListSearchResultDto
import com.oye.moviepedia.data.dto.SearchDto
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import javax.inject.Singleton

interface MovieDataSource {

    fun fetchMovies(primaryDateRelease: LocalDate?, sortBy: String?, includeAdult: Boolean = false): List<MovieDto>

    fun fetchNowPlayingMovies(): List<MovieDto>

    fun fetchCreditsMovies(id: Int): CreditsResultDto

    fun fetchPopularMovies(): List<MovieDto>

    fun fetchSearchResult(query: String): List<SearchDto>

}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieDataSourceModule {

    @Binds
    @Singleton
    abstract fun provideMovieDataSource(dataSource: RemoteMovieDataSource): MovieDataSource
}