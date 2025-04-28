package com.example.moviedb2025.database

import com.example.moviedb2025.models.ExternalIdsResponse
import com.example.moviedb2025.models.MovieResponse
import com.example.moviedb2025.models.ReviewResponse
import com.example.moviedb2025.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getExternalIds(movieId: Long): ExternalIdsResponse
    suspend fun getReviews(movieId: Long): ReviewResponse
}

class NetworkMoviesRepository(private val apiService: MovieDBApiService) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

    override suspend fun getExternalIds(movieId: Long): ExternalIdsResponse {
        return apiService.getExternalIds(movieId)
    }

    override suspend fun getReviews(movieId: Long): ReviewResponse {
        return apiService.getReviews(movieId)
    }
}
