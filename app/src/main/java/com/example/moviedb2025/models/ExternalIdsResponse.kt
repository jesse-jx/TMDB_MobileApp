package com.example.moviedb2025.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalIdsResponse(
    @SerialName(value = "imdb_id")
    val imdbId: String?
)