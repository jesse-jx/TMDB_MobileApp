package com.example.moviedb2025.database

import com.example.moviedb2025.models.ExternalIdsResponse
import com.example.moviedb2025.models.MovieResponse
import com.example.moviedb2025.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getExternalIds(movieId: Long): ExternalIdsResponse
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
}
