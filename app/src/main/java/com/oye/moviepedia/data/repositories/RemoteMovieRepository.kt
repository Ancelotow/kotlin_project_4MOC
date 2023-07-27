package com.oye.moviepedia.data.repositories

import android.util.Log
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.entities.MovieDetails
import com.oye.moviepedia.domain.entities.SearchResult
import com.oye.moviepedia.domain.repositories.MovieRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class RemoteMovieRepository @Inject constructor(
    private val dataSource: MovieDataSource
) : MovieRepository {

    override fun getUpcomingMovies(): List<Movie> {
        val moviesDto = dataSource.fetchMovies(
            primaryDateRelease = LocalDate.now(),
            sortBy = "popularity.desc",
        )
        val movies = ArrayList<Movie>()
        for (movie in moviesDto) {
            val credits = dataSource.fetchCreditsMovies(movie.id)
            val director: String = try {
                credits.crew.first { e -> e.job == "Director" }.name
            } catch (e: NoSuchElementException) {
                "Unknow"
            }
            movies.add(movie.toMovie(director))
        }
        return movies;
    }

    override fun getNewMovies(): List<Movie> {
        val moviesDto = dataSource.fetchNowPlayingMovies()
        val dateNow = LocalDate.now()
        val newMoviesDto =
            moviesDto.filter { e -> ChronoUnit.DAYS.between(e.release_date, dateNow) <= 7 }
        val newMovies = ArrayList<Movie>()
        for (movie in newMoviesDto) {
            val credits = dataSource.fetchCreditsMovies(movie.id)
            val director: String = try {
                credits.crew.first { e -> e.job == "Director" }.name
            } catch (e: NoSuchElementException) {
                "Unknow"
            }
            newMovies.add(movie.toMovie(director))
        }
        return newMovies
    }

    override fun getNowPlayingMovies(): List<Movie> {
        val moviesDto = dataSource.fetchNowPlayingMovies()
        val movies = ArrayList<Movie>()
        for (movie in moviesDto) {
            val credits = dataSource.fetchCreditsMovies(movie.id)
            val director: String = try {
                credits.crew.first { e -> e.job == "Director" }.name
            } catch (e: NoSuchElementException) {
                "Unknow"
            }
            movies.add(movie.toMovie(director))
        }
        return movies;
    }

    override fun getPopularMovies(): List<Movie> {
        val moviesDto = dataSource.fetchPopularMovies()
        val movies = ArrayList<Movie>()
        for (movie in moviesDto) {
            val credits = dataSource.fetchCreditsMovies(movie.id)
            val director: String = try {
                credits.crew.first { e -> e.job == "Director" }.name
            } catch (e: NoSuchElementException) {
                "Unknow"
            }
            movies.add(movie.toMovie(director))
        }
        return movies;
    }

    override fun getMovieDetails(id: Int): MovieDetails? {
        dataSource.getMovieDetails(id)?.let { movieDto ->
            val credits = dataSource.fetchCreditsMovies(movieDto.id)
            val trailerDto = dataSource.getMovieTrailers(id)
            val director: String = try {
                credits.crew.first { e -> e.job == "Director" }.name
            } catch (e: NoSuchElementException) {
                "Unknow"
            }
            var trailer = trailerDto?.results?.firstOrNull { t ->
                t.site == "YouTube" && t.type == "Trailer" && t.official
            }
            if (trailer == null) {
                trailer = trailerDto?.results?.firstOrNull { t ->
                    t.site == "YouTube" && t.type == "Trailer"
                }
            }
            return movieDto.toMovieDetail(credits.cast, director, trailer?.key)
        }
        return null
    }

    override fun getSearchResult(query: String): List<SearchResult> {
        val tvGenre = dataSource.fetchTvGenres().map { it.toGenre() }
        val movieGenre = dataSource.fetchMovieGenres().map { it.toGenre() }
        val resultDto = dataSource.fetchSearchResult(
            query = query,
        )
        return resultDto.map { it.toSearchResult(tvGenre, movieGenre) }
    }

}