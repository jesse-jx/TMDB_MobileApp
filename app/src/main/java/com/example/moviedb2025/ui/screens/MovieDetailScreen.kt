@file:OptIn(ExperimentalLayoutApi::class)

package com.example.moviedb2025.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviedb2025.utils.Constants
import com.example.moviedb2025.database.Genres
import com.example.moviedb2025.database.MoviesRepository
import com.example.moviedb2025.network.MovieDBApiService
import com.example.moviedb2025.viewmodel.SelectedMovieUiState

@Composable
fun MovieDetailScreen(selectedMovieUiState: SelectedMovieUiState,
                      modifier: Modifier) {
    //val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        when (selectedMovieUiState) {
            is SelectedMovieUiState.Success -> {
                val movie = selectedMovieUiState.movie
                val tmdbUrl = "https://www.themoviedb.org/movie/${movie.id}"
                val imdbUrl = "https://www.imdb.com/title/${movie.imdbId}"

                Box(modifier = modifier) {
                    AsyncImage(
                        model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_BASE_WIDTH + movie.backdropPath,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 25.dp, top = 25.dp, end = 25.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Text(
                        text = movie.releaseDate,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.size(15.dp))

                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    FlowRow {
                        Genres().getGenreNames(movie.genreIds).forEach { genre ->
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.wrapContentSize().padding(start = 3.dp)
                            ) {
                                Text(
                                    text = genre,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF176)) // IMDb yellow
                        ) {
                            Box(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "View on IMDb",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tmdbUrl))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9)) // TMDB light blue
                        ) {
                            Box(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "View on TMDB",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }

            is SelectedMovieUiState.Loading -> {
                Text(
                    text = "loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is SelectedMovieUiState.Error -> {
                Text(
                    text = "Error!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}