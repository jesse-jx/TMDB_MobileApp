package com.example.moviedb2025.database

import android.util.Log
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
        val response = apiService.getPopularMovies()
        val taggedMovies = response.results.map { it.copy(viewType = "popular") }
        return response.copy(results = taggedMovies)
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        val response = apiService.getTopRatedMovies()
        val taggedMovies = response.results.map { it.copy(viewType = "toprated") }
        return response.copy(results = taggedMovies)
    }

    override suspend fun getExternalIds(movieId: Long): ExternalIdsResponse {
        return apiService.getExternalIds(movieId)
    }

    override suspend fun getReviews(movieId: Long): ReviewResponse {
        return apiService.getReviews(movieId)
    }

    override suspend fun getMoviesByViewType(viewType: String): List<Movie> {
        val cached = movieDao.getMoviesByViewType(viewType)
        Log.d("MoviesRepository", "Loaded ${cached.size} cached movies for $viewType")
        return cached
    }

    override suspend fun cacheMovies(movies: List<Movie>, viewType: String) {
        Log.d("MoviesRepository", "Caching ${movies.size} movies with viewType = $viewType")
        movies.forEach {
            Log.d("MoviesRepository", " -> Movie ID: ${it.id}, viewType: ${it.viewType}")
        }
        //movieDao.insertMovies(movies) // Insert new movies
        //movieDao.deleteMoviesExcept(viewType)
        // Delete old cache
        movieDao.deleteMovies()
        movieDao.insertMovies(movies)
    }
}
