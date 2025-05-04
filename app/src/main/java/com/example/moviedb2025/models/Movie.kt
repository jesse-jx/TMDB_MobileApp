package com.example.moviedb2025.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moviedb2025.database.Converters
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Entity(tableName = "movies")
@Serializable
data class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    var id: Long = 0L,

    @SerialName(value = "title")
    var title: String,

    @SerialName(value = "poster_path")
    var posterPath: String,

    @SerialName(value = "backdrop_path")
    var backdropPath: String?=null,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "overview")
    var overview: String,

    @SerialName(value = "genre_ids")
    @TypeConverters(Converters::class)
    var genreIds: List<Int>,

    @SerialName(value = "imdb_id")
    var imdbId: String? = null,

    var viewType: String = ""
)


