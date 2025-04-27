package com.example.moviedb2025.network

import com.example.moviedb2025.models.ExternalIdsResponse
import com.example.moviedb2025.models.MovieResponse
import com.example.moviedb2025.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBApiService {

    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("{movie_id}/external_ids")
    suspend fun getExternalIds(
        @Path("movie_id") movieId: Long,
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): ExternalIdsResponse
}