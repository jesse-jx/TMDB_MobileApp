package com.example.moviedb2025.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.utils.Constants
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign
import com.example.moviedb2025.database.Genres
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.moviedb2025.ui.theme.darkPurple
import com.example.moviedb2025.ui.theme.lightPurple
import com.example.moviedb2025.ui.theme.onLightPurple
import com.example.moviedb2025.viewmodel.MovieDBViewModel
import com.example.moviedb2025.viewmodel.MovieListUiState
import com.google.common.collect.Multimaps.index

enum class MovieTab(val title: String) {
    Popular("Popular"),
    TopRated("Top Rated")
}

@Composable
fun MovieListGridScreen(movieListUiState: MovieListUiState,
                        onMovieListItemClicked: (Movie) -> Unit,
                        selectedTab: MovieTab,
                        onTabSelected: (MovieTab) -> Unit,
                        modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        val selectedColor = darkPurple
        val unselectedColor = Color.Gray
        val indicatorColor = darkPurple

        TabRow(selectedTabIndex = selectedTab.ordinal,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                    color = indicatorColor
                )
            } )
            {
            MovieTab.values().forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    text = { Text(tab.title,
                        color = if (tab == selectedTab) selectedColor else unselectedColor) }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            when (movieListUiState) {
                is MovieListUiState.Success -> {
                    items(movieListUiState.movies) { movie ->
                        MovieListItemCard2(
                            movie = movie,
                            onMovieListItemClicked = onMovieListItemClicked,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is MovieListUiState.Loading -> {
                    item {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is MovieListUiState.Error -> {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListItemCard2(
        movie: Movie,
        onMovieListItemClicked: (Movie) -> Unit,
        modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Card(
        onClick = { onMovieListItemClicked(movie) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_BASE_WIDTH + movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth().basicMarquee(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 15.dp)) {
                Row(
                    modifier = Modifier
                            .horizontalScroll(scrollState)
                ) {
                    Genres().getGenreNames(movie.genreIds).forEach { genre ->
                        Surface(
                            color = lightPurple,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.wrapContentSize().padding(start = 3.dp)
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodySmall,
                                color = onLightPurple,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}
