package com.example.tmdb.ui.screen.movie

import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tmdb.model.MoviesResponse
import com.example.tmdb.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.io.IOException



class MoviesViewModel(movieRepo: MovieRepository) : ViewModel() {

    val moviePagingFlow = movieRepo.getPopularMovies()
        .cachedIn(viewModelScope)

    companion object {
        fun factory(movieRepo: MovieRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                        return MoviesViewModel(movieRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}