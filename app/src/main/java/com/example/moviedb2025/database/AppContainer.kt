package com.example.moviedb2025.database

import android.content.Context
import androidx.room.Room
import com.example.moviedb2025.network.MovieDBApiService
import com.example.moviedb2025.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val moviesRepository: MoviesRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val applicationContext = context.applicationContext

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val movieDBJson = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(movieDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.MOVIE_LIST_BASE_URL)
        .build()

    private val retrofitService: MovieDBApiService by lazy {
        retrofit.create(MovieDBApiService::class.java)
    }

    val database: MovieDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }

    override val moviesRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(retrofitService, database.movieDao())
    }
}