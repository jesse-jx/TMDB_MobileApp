@file:OptIn(ExperimentalLayoutApi::class)

package com.example.moviedb2025.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.utils.Constants
import com.example.myapplication0.ui.screens.MovieDBScreen
import androidx.core.net.toUri
import com.example.moviedb2025.database.Genres

@Composable
fun MovieDetailScreen(movie: Movie,
                      modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val tmdbUrl = "https://www.themoviedb.org/movie/${movie.id}"
    val imdbUrl = "https://www.imdb.com/title/${movie.imdbId}"

    Column{
        Box {
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_BASE_WIDTH + movie.backdropPath,
                contentDescription = movie.title,
                modifier = Modifier, //complete weight
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.padding(start = 25.dp, top = 25.dp, end = 25.dp)){
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, tmdbUrl.toUri())
                    context.startActivity(intent)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.OpenInNew,
                        contentDescription = "Open External Link",
                        tint = Color.Black
                    )
                }
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
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, imdbUrl.toUri())
                context.startActivity(intent)
            }) {
                Text(
                    text = "Open in IMDb",
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline)
                )
            }
            //Spacer(modifier = Modifier.size(20.dp))
        }
    }
}