package com.example.tmdb.ui.screen.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.model.MoviesResponse
import com.example.tmdb.network.ApiInterface
import com.example.tmdb.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _moviesList = MutableStateFlow<MoviesResponse?>(null)
    val moviesList: StateFlow<MoviesResponse?> = _moviesList.asStateFlow()

    fun fetchMovies() {
        viewModelScope.launch {
            _errorMessage.value = null

            try {
                _moviesList.value = ApiService.create(ApiInterface::class.java).fetchMovieList()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
            }
        }
    }
}