package com.example.moviedb2025.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WatchListScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Watchlist", style = MaterialTheme.typography.headlineSmall)
    }
}