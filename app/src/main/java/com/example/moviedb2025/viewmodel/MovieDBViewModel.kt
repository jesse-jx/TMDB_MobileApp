package com.example.moviedb2025.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviedb2025.MovieDBApplication
import com.example.moviedb2025.database.MovieDBUIState
import com.example.moviedb2025.database.MoviesRepository
import com.example.moviedb2025.models.Movie
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
}

sealed interface SelectedMovieUiState {
    data class Success(val movie: Movie) : SelectedMovieUiState
    object Error : SelectedMovieUiState
    object Loading: SelectedMovieUiState
}

class MovieDBViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    init {
        getPopularMovies()
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                //MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)

                val movies = moviesRepository.getTopRatedMovies().results
                // For each movie, fetch its external IDs
                val enrichedMovies = movies.map { movie ->
                    async {
                        try {
                            val externalIds = moviesRepository.getExternalIds(movie.id)
                            movie.copy(imdbId = externalIds.imdbId)
                        } catch (e: Exception) {
                            // In case the external ID fetch fails, return movie unchanged
                            movie
                        }
                    }
                }.awaitAll()

                MovieListUiState.Success(enrichedMovies)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                //MovieListUiState.Success(moviesRepository.getPopularMovies().results)

                val movies = moviesRepository.getPopularMovies().results
                // For each movie, fetch its external IDs
                val enrichedMovies = movies.map { movie ->
                    async {
                        try {
                            val externalIds = moviesRepository.getExternalIds(movie.id)
                            movie.copy(imdbId = externalIds.imdbId)
                        } catch (e: Exception) {
                            // In case the external ID fetch fails, return movie unchanged
                            movie
                        }
                    }
                }.awaitAll()

                MovieListUiState.Success(enrichedMovies)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun setSelectedMovie(movie: Movie) {
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(movie)
            } catch (e: IOException) {
                SelectedMovieUiState.Error
            } catch (e: HttpException) {
                SelectedMovieUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                MovieDBViewModel(moviesRepository = moviesRepository)
            }
        }
    }
}