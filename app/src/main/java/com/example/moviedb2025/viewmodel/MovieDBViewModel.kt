package com.example.moviedb2025.viewmodel

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.example.moviedb2025.MovieDBApplication
import com.example.moviedb2025.database.MovieDBUIState
import com.example.moviedb2025.database.MovieFetchWorker
import com.example.moviedb2025.database.MoviesRepository
import com.example.moviedb2025.models.Movie
import com.example.moviedb2025.models.Review
import com.example.moviedb2025.ui.screens.MovieTab
import com.example.moviedb2025.utils.NetworkMonitor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    object Error : MovieListUiState
    object Loading: MovieListUiState
    object NoConnection : MovieListUiState
}

sealed interface SelectedMovieUiState {
    data class Success(val movie: Movie) : SelectedMovieUiState
    object Error : SelectedMovieUiState
    object Loading: SelectedMovieUiState
}

sealed interface ReviewListUiState {
    data class Success(val reviews: List<Review>) : ReviewListUiState
    object Loading : ReviewListUiState
    object Error : ReviewListUiState
}

class MovieDBViewModel(private val application: Application, private val moviesRepository: MoviesRepository) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    var reviewListUiState: ReviewListUiState by mutableStateOf(ReviewListUiState.Loading)
        private set

    var selectedTab: MovieTab by mutableStateOf(MovieTab.Popular)
        private set

    private val tabsFetchedOnline = mutableSetOf<String>()

    fun initSelectedTab(context: Context) {
        val prefs = context.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
        val saved = prefs.getString("selected_tab", MovieTab.Popular.name)
        val savedTab = MovieTab.valueOf(saved!!)
        selectedTab = savedTab
        loadMovies(savedTab, context)
    }

    //fun getTopRatedMovies() {
    //    viewModelScope.launch {
    //        movieListUiState = MovieListUiState.Loading
    //        movieListUiState = try {
                //MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)

    //            val movies = moviesRepository.getTopRatedMovies().results
                // For each movie, fetch its external IDs
    //             val enrichedMovies = movies.map { movie ->
    //                async {
    //                    try {
    //                        val externalIds = moviesRepository.getExternalIds(movie.id)
    //                        movie.copy(imdbId = externalIds.imdbId)
    //                     } catch (e: Exception) {
                            // In case the external ID fetch fails, return movie unchanged
    //                        movie
    //                     }
    //                }
    //            }.awaitAll()

    //             MovieListUiState.Success(enrichedMovies)
    //        } catch (e: IOException) {
    //            MovieListUiState.Error
    //        } catch (e: HttpException) {
    //             MovieListUiState.Error
    //        }
    //    }
//    }

    //fun getPopularMovies() {
    //    viewModelScope.launch {
    //        movieListUiState = MovieListUiState.Loading
    //        movieListUiState = try {
    //            val movies = moviesRepository.getPopularMovies().results
    //            val imdbMovies = movies.map { movie ->
    //                async {
    //                    try {
    //                        val externalIds = moviesRepository.getExternalIds(movie.id)
    //                        movie.copy(imdbId = externalIds.imdbId)
    //                    } catch (e: Exception) {
    //                       movie
    //                    }
    //                }
    //            }.awaitAll()

    //            MovieListUiState.Success(imdbMovies)
    //         } catch (e: IOException) {
    //            MovieListUiState.Error
    //        } catch (e: HttpException) {
    //            MovieListUiState.Error
    //        }
    //    }
    //}

    fun setSelectedMovie(movie: Movie) {
        selectedMovieUiState = SelectedMovieUiState.Success(movie)
    }

    fun getReviews(movieId: Long) {
        viewModelScope.launch {
            println("1")
            reviewListUiState = ReviewListUiState.Loading
            reviewListUiState = try {
                ReviewListUiState.Success(moviesRepository.getReviews(movieId).results)
            } catch (e: IOException) {
                ReviewListUiState.Error
            } catch (e: HttpException) {
                ReviewListUiState.Error
            }
        }
    }

    fun onTabSelected(tab: MovieTab, context: Context) {
        selectedTab = tab
        context.getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
            .edit() {
                putString("selected_tab", tab.name)
            }
        loadMovies(tab, context)
    }

    fun loadMovies(type: MovieTab, context: Context) {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading

            val viewType = type.name.lowercase()

            try {
                if (NetworkMonitor.isConnected(application.applicationContext)) {
                    tabsFetchedOnline.add(viewType)
                    val response = when (type) {
                        MovieTab.Popular -> moviesRepository.getPopularMovies()
                        MovieTab.TopRated -> moviesRepository.getTopRatedMovies()
                    }

                    val enriched = response.results.map { movie ->
                        async {
                            try {
                                val externalIds = moviesRepository.getExternalIds(movie.id)
                                movie.copy(imdbId = externalIds.imdbId, viewType = viewType)
                            } catch (e: Exception) {
                                movie.copy(viewType = viewType)
                            }
                        }
                    }.awaitAll()

                    // Cache the fetched movies
                    moviesRepository.cacheMovies(enriched, viewType)
                    val cached = moviesRepository.getMoviesByViewType(viewType)
                    if (cached.isNotEmpty()) {
                        movieListUiState = MovieListUiState.Success(cached)
                    } else {
                        movieListUiState = MovieListUiState.Error // fallback if Room insert failed
                    }

                } else {
                    if (viewType in tabsFetchedOnline) {
                        val cachedMovies = moviesRepository.getMoviesByViewType(viewType)
                        if (cachedMovies.isNotEmpty()) {
                            movieListUiState = MovieListUiState.Success(cachedMovies)
                        } else {
                            movieListUiState = MovieListUiState.NoConnection
                        }
                    } else {
                        movieListUiState = MovieListUiState.NoConnection
                    }
                }
                enqueueMovieWorker(viewType)
            } catch (e: Exception) {
                e.printStackTrace()
                movieListUiState = MovieListUiState.Error
            }
        }
    }

    private fun enqueueMovieWorker(movieType: String) {
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<MovieFetchWorker>()
            .setInputData(workDataOf("movieType" to movieType))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)  // Only run if network is available
                    .build()
            )
            .build()

        WorkManager.getInstance(application.applicationContext).enqueue(workRequest)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                MovieDBViewModel(application = application, moviesRepository = moviesRepository)
            }
        }
    }
}