package com.example.moviedb2025.models

data class Movie(
    var id: Long = 0L,
    var title: String,
    var posterPath: String,
    var backdropPath: String,
    var releaseDate: String,
    var overview: String,
    var genreTag: List<Int>,
    var imdbId: String
)