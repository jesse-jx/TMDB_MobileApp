package com.example.moviedb2025.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviedb2025.models.Review
import com.example.moviedb2025.viewmodel.ReviewListUiState

@Composable
fun ReviewsScreen(
    reviewListUiState: ReviewListUiState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (reviewListUiState) {
            is ReviewListUiState.Success -> {
                items(reviewListUiState.reviews) { review ->
                    ReviewCard(review)
                }
            }

            is ReviewListUiState.Loading -> {
                item {
                    Text(
                        text = "loading...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is ReviewListUiState.Error -> {
                item {
                    Text(
                        text = "Error!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review.author,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.content,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))

            review.authorDetails.rating?.let { rating ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.1f", rating),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.padding(start = 8.dp))
                    val filledStars = rating.toInt()
                    val emptyStars = 10 - filledStars
                    repeat(filledStars) {
                        Text(
                            text = "★",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFFD700)
                        )
                    }
                    repeat(emptyStars) {
                        Text(
                            text = "☆",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            } ?: Text(
                text = "No Rating",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}