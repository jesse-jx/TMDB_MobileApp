package com.example.moviedb2025.database

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moviedb2025.MovieDBApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "MovieFetchWorker"
private const val DELAY_TIME_MILLIS = 3000L

class MovieFetchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val movieType = inputData.getString("movieType") ?: return Result.failure()
        val moviesRepository = (applicationContext as MovieDBApplication).container.moviesRepository

        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS)
            return@withContext try {
                when (movieType) {
                    "popular" -> {
                        val result = moviesRepository.getPopularMovies()
                        moviesRepository.cacheMovies(result.results, "popular")
                    }
                    "topRated" -> {
                        val result = moviesRepository.getTopRatedMovies()
                        moviesRepository.cacheMovies(result.results, "topRated")
                    }
                    else -> null
                }?.let {
                    // Cache the result using Room or DataStore
                    Log.d(TAG, "Successfully fetched $movieType movies.")
                    Result.success()
                } ?: Result.failure()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching movies: ${e.message}", e)
                Result.retry()
            }
        }
    }
}