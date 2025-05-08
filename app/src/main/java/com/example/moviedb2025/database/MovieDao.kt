package com.example.moviedb2025.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb2025.models.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE viewType = :viewType")
    suspend fun getMoviesByViewType(viewType: String): List<Movie>

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()


}