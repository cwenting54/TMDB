package com.example.tmdb.ui.screen.favoriteMovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.BuildConfig
import com.example.tmdb.model.FavoriteMovieRequest
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.network.ApiInterface
import com.example.tmdb.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteMovieViewModel : ViewModel() {
    private val _favoriteList = MutableStateFlow(emptyList<MoviesDetails>())
    private val accountId = BuildConfig.accountId.toInt()
    private val sessionId = BuildConfig.sessionId

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchFavoriteMovies()
    }

    suspend fun isMovieFavorited(movieId: Int): Boolean {
        return withContext(Dispatchers.Default) {
            _favoriteList.value.any { it.id == movieId }
        }
    }
    private fun refreshFavoriteMovies() {
        viewModelScope.launch {
            fetchFavoriteMovies()
        }
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

    fun toggleFavoriteMovie(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            val request = FavoriteMovieRequest(media_id = movieId, favorite = isFavorite)
            try {
                ApiService.create(ApiInterface::class.java)
                    .toggleFavroiteMovie(accountId, sessionId, request)
                refreshFavoriteMovies()
            } catch (e: Exception) {
                Log.e("FavoriteMovieViewModel", "Error adding favorite movie: ${e.message}")
            }
        }
    }
}