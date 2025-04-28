package com.example.moviedb2025.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "page")
    val page: Int,
    @SerialName(value = "results")
    val results: List<Review>
)