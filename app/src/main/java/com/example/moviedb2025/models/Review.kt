package com.example.moviedb2025.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName(value = "author")
    var author: String,
    @SerialName(value = "content")
    var content: String,
    @SerialName("author_details")
    val authorDetails: AuthorDetails
)

@Serializable
data class AuthorDetails(
    @SerialName("rating")
    val rating: Float? = null
)