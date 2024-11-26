package com.example.tmdb.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdb.model.MoviesDetails


class MoviePagingSource(private val apiService: ApiService) : PagingSource<Int, MoviesDetails>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesDetails> {
        val page = params.key ?: 1
        return try {
            val response = apiService.create(ApiInterface::class.java).fetchMovieList(page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, MoviesDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
