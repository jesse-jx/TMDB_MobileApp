package com.example.moviedb2025.database

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moviedb2025.MovieDBApplication

class MovieFetchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val movieType = inputData.getString("movieType") ?: return Result.failure()
        val moviesRepository = (applicationContext as MovieDBApplication).container.moviesRepository

        return try {
            when (movieType) {
                "popular" -> moviesRepository.getPopularMovies()
                "topRated" -> moviesRepository.getTopRatedMovies()
                else -> null
            }?.let {
                // Cache the result using Room or DataStore
                Result.success()
            } ?: Result.failure()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}