package com.oye.moviepedia.data.repositories

import androidx.compose.ui.text.toLowerCase
import com.oye.moviepedia.data.data_sources.MovieDataSource
import com.oye.moviepedia.domain.entities.Movie
import com.oye.moviepedia.domain.repositories.MovieRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import javax.inject.Inject

class RemoteMovieRepository @Inject constructor(
    private val dataSource: MovieDataSource
) : MovieRepository {

    override fun getNewMovie(): List<Movie> {
        val moviesDto = dataSource.fetchNowPlayingMovies()
        val dateNow = LocalDate.now()
        val newMoviesDto = moviesDto.filter { e -> ChronoUnit.DAYS.between(e.release_date, dateNow) <= 7 }
        val newMovies = ArrayList<Movie>()
        for (movie in newMoviesDto) {
            val credits = dataSource.fetchCreditsMovies(movie.id)
            val director = credits.crew.first { e -> e.job == "Director" }
            newMovies.add(Movie.fromMovieDto(movie, director.name))
        }
        return newMovies;
    }

}