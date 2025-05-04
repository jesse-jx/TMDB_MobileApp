package com.example.moviedb2025.database

import com.example.moviedb2025.models.ExternalIdsResponse
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.models.MovieResponse
import com.example.moviedb2025.models.ReviewResponse
import com.example.moviedb2025.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getExternalIds(movieId: Long): ExternalIdsResponse
    suspend fun getReviews(movieId: Long): ReviewResponse
    suspend fun cacheMovies(movies: List<Movie>, viewType: String)
    suspend fun getMoviesByViewType(viewType: String): List<Movie>
}

class NetworkMoviesRepository(private val apiService: MovieDBApiService,
                              private val movieDao: MovieDao) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        val movies = apiService.getPopularMovies()
        movieDao.insertMovies(movies.results)
        return movies
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        val movies = apiService.getTopRatedMovies()
        movieDao.insertMovies(movies.results)
        return movies
    }

    override suspend fun getExternalIds(movieId: Long): ExternalIdsResponse {
        return apiService.getExternalIds(movieId)
    }

    override suspend fun getReviews(movieId: Long): ReviewResponse {
        return apiService.getReviews(movieId)
    }

    override suspend fun getMoviesByViewType(viewType: String): List<Movie> {
        return movieDao.getMoviesByViewType(viewType)
    }

    override suspend fun cacheMovies(movies: List<Movie>, viewType: String) {
        movieDao.deleteMoviesExcept(viewType) // Delete old cache
        movieDao.insertMovies(movies) // Insert new movies
    }
}
