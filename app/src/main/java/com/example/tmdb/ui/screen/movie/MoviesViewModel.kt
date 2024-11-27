package com.example.tmdb.ui.screen.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tmdb.network.ApiService
import com.example.tmdb.repository.MovieRepository


class MoviesViewModel(
    private val apiService: ApiService = ApiService,
    movieRepo: MovieRepository = MovieRepository(apiService)
) : ViewModel() {

    val moviePagingFlow = movieRepo.getPopularMovies()
        .cachedIn(viewModelScope)
}