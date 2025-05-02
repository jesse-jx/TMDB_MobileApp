package com.example.moviedb2025.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Movie(
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
    var genreIds: List<Int>,

    @SerialName(value = "imdb_id")
    var imdbId: String? = null
)


