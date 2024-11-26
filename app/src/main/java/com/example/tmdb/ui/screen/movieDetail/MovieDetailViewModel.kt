package com.example.tmdb.ui.screen.movieDetail

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

class MovieDetailViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?>  = _errorMessage.asStateFlow()

    private val _moviesDetail = MutableStateFlow<MoviesDetails?>(null)
    val moviesDetail: StateFlow<MoviesDetails?> = _moviesDetail.asStateFlow()

    fun fetchMovieDetail(movieId: Int){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _moviesDetail.value = ApiService.create(ApiInterface::class.java).fetchMovieDetail(movieId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}