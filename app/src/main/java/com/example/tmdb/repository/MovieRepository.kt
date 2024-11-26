package com.example.tmdb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.network.ApiService
import com.example.tmdb.network.MoviePagingSource
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val apiService: ApiService) {
    fun getPopularMovies(): Flow<PagingData<MoviesDetails>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).flow
    }
}