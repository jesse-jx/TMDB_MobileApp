@file:OptIn(ExperimentalLayoutApi::class)

package com.example.moviedb2025.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.database.Genres
import com.example.moviedb2025.utils.Constants


@Composable
fun MovieListScreen(
    movieList: List<Movie>,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(movieList) { movie ->
            MovieListItemCard(movie = movie, onMovieListItemClicked, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun MovieListItemCard(movie: Movie,
                      onMovieListItemClicked: (Movie) -> Unit,
                      modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Card(modifier = modifier,
        onClick = {
            onMovieListItemClicked(movie)
        },
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White))
    {
        Row {
            Box {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_BASE_WIDTH + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier
//                        .width(92.dp)
                        .height(170.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
            Column {
                Spacer(modifier = androidx.compose.ui.Modifier.size(10.dp))

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth().basicMarquee()
                )
                Spacer(modifier = androidx.compose.ui.Modifier.size(8.dp))

                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 15.dp)
                )
                Spacer(modifier = androidx.compose.ui.Modifier.size(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 15.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = androidx.compose.ui.Modifier.size(8.dp))

                Box(modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 15.dp)) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                    ) {
                        Genres().getGenreNames(movie.genreTag).forEach { genre ->
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
                    Spacer(modifier = androidx.compose.ui.Modifier.size(8.dp))
                }
            }
        }
    }
}