package com.example.moviedb2025.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MovieResponse(
    @SerialName(value = "page")
    var page: Int = 0,

    @SerialName(value = "results")
    var results: List<Movie> = listOf(),

    @SerialName(value = "total_pages")
    var total_pages: Int = 0,

    @SerialName(value = "total_results")
    var total_results: Int = 0,)