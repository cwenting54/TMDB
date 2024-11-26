package com.example.tmdb.ui.screen.favoriteMovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.BuildConfig
import com.example.tmdb.model.FavoriteMovieRequest
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.network.ApiInterface
import com.example.tmdb.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteMovieViewModel : ViewModel() {
    private val _favoriteList = MutableStateFlow(emptyList<MoviesDetails>())
    val favoriteList: StateFlow<List<MoviesDetails>> = _favoriteList.asStateFlow()
    private val accountId = BuildConfig.accountId.toInt()
    private val sessionId = BuildConfig.sessionId

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchFavoriteMovies()
    }

    fun isMovieFavorited(movieId: Int): Boolean {
        return _favoriteList.value.any { it.id == movieId }
    }

    private fun fetchFavoriteMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = ApiService.create(ApiInterface::class.java)
                    .fetchFavoriteList(accountId, sessionId)
                _favoriteList.value = response.results
                _isLoading.value = false
            } catch (e: Exception) {
                _favoriteList.value = emptyList()
            }
        }
    }

    fun toggleFavoriteMovie(movie: MoviesDetails, isFavorite: Boolean) {
        viewModelScope.launch {
            val request = FavoriteMovieRequest(media_id = movie.id, favorite = isFavorite)
            try {
                ApiService.create(ApiInterface::class.java)
                    .toggleFavroiteMovie(accountId, sessionId, request)
                _favoriteList.value = if(isFavorite) _favoriteList.value + movie else _favoriteList.value.filter { it.id != movie.id }
            } catch (e: Exception) {
                Log.e("FavoriteMovieViewModel", "Error adding favorite movie: ${e.message}")
            }
        }
    }
}