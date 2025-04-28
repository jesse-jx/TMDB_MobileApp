@file:OptIn(ExperimentalLayoutApi::class)

package com.example.moviedb2025.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.moviedb2025.utils.Constants
import com.example.moviedb2025.database.Genres
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.viewmodel.SelectedMovieUiState

@Composable
fun MovieDetailScreen(selectedMovieUiState: SelectedMovieUiState,
                      onToReviewsClicked: (Movie) -> Unit,
                      modifier: Modifier) {
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

                TrailerList(
                    videos = listOf(
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                    )
                )

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                onToReviewsClicked(movie)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF514065)
                            ),
                        ) {
                            Text("To Reviews →")
                        }
                    }
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

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    context: Context,
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT // <- Important!
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // no hardcoded height
    )
}

@Composable
fun TrailerList(videos: List<String>) {
    val context = LocalContext.current

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { videoUrl ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .aspectRatio(16f / 9f)
            ) {
                VideoPlayer(
                    context = context,
                    videoUrl = videoUrl,
                    modifier = Modifier
                        .width(250.dp)
                        .aspectRatio(16f / 9f)
                )
            }
        }
    }
}