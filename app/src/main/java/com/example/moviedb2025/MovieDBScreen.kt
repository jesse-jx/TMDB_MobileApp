@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moviedb2025

import kotlin.OptIn
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviedb2025.ui.screens.FavouritesScreen
import com.example.moviedb2025.ui.screens.MovieDetailScreen
import com.example.moviedb2025.ui.screens.MovieListGridScreen
import com.example.moviedb2025.ui.screens.MovieTab
import com.example.moviedb2025.ui.screens.ReviewsScreen
import com.example.moviedb2025.ui.screens.WatchListScreen
import com.example.moviedb2025.ui.theme.darkPurple
import com.example.moviedb2025.utils.NetworkMonitor
import com.example.moviedb2025.viewmodel.MovieDBViewModel
import com.example.moviedb2025.viewmodel.MovieListUiState

enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail),
    Favourites(title = R.string.favourited_movies),
    Watchlist(title = R.string.watchlist),
    Reviews(title = R.string.reviews)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currScreen: MovieDBScreen,
    canNavigateBack:Boolean,
    navigateUp: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    CenterAlignedTopAppBar(
        title = {Text(stringResource(currScreen.title), style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = darkPurple
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }

            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = "Menu"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Watchlist") },
                    onClick = {
                        expanded = false
                        navController.navigate(MovieDBScreen.Watchlist.name)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Favourites") },
                    onClick = {
                        expanded = false
                        navController.navigate(MovieDBScreen.Favourites.name)
                    }
                )
            }
        }
    )
}

@Composable
fun MovieDbApp(navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )
    val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)

    val tabs = MovieTab.values().toList()
    val selectedTab = movieDBViewModel.selectedTab

    // Reload when internet is restored and UI state is NoConnection
    LaunchedEffect(Unit) {
        movieDBViewModel.initSelectedTab(context)
        NetworkMonitor.registerNetworkCallback(
            context = context,
            onAvailable = {
                if (movieDBViewModel.movieListUiState is MovieListUiState.NoConnection) {
                    movieDBViewModel.loadMovies(movieDBViewModel.selectedTab, context)
                }
            },
            onLost = {

            }
        )
    }


    Scaffold(
        topBar = {
            MovieDBAppBar(currScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = MovieDBScreen.List.name){
                MovieListGridScreen(
                    movieListUiState = movieDBViewModel.movieListUiState,
                    onMovieListItemClicked = { movie ->
                        movieDBViewModel.setSelectedMovie(movie)
                        navController.navigate(MovieDBScreen.Detail.name)
                    },
                    selectedTab = selectedTab,
                    onTabSelected = { tab -> movieDBViewModel.onTabSelected(tab, context) },
                    modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
                )
            }
            composable(route = MovieDBScreen.Detail.name) {
                MovieDetailScreen(
                    selectedMovieUiState = movieDBViewModel.selectedMovieUiState,
                    onToReviewsClicked = { movie ->
                        movieDBViewModel.getReviews(movie.id)
                        navController.navigate(MovieDBScreen.Reviews.name)
                    },
                    modifier = Modifier
                )
            }
            composable(route = MovieDBScreen.Favourites.name) {
                FavouritesScreen(modifier = Modifier.fillMaxSize())
            }
            composable(route = MovieDBScreen.Watchlist.name) {
                WatchListScreen()
            }
//            composable(route = "${MovieDBScreen.Reviews.name}/{movieId}") { backStackEntry ->
//                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
//                if (movieId != null) {
//                    movieDBViewModel.getReviews(movieId)
//                    ReviewsScreen(reviewListUiState = movieDBViewModel.reviewListUiState)
//                }
//            }
            composable(route = MovieDBScreen.Reviews.name) {
                ReviewsScreen(
                    reviewListUiState = movieDBViewModel.reviewListUiState,
                    modifier = Modifier
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MovieDB2025Theme {
//        MovieListItemCard2(
//            movie = Movie(
//                2,
//                "Captain America: Brave New World",
//                "/pzIddUEMWhWzfvLI3TwxUG2wGoi.jpg",
//                "/gsQJOfeW45KLiQeEIsom94QPQwb.jpg",
//                "2025-02-12",
//                "When a group of radical activists take over an energy company's annual gala, seizing 300 hostages, an ex-soldier turned window cleaner suspended 50 storeys up on the outside of the building must save those trapped inside, including her younger brother.",
//                listOf(28, 53, 878)
//            ), {}
//        )
//    }
//}
