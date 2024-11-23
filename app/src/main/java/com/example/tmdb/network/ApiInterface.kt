package com.example.tmdb.network

import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("movie/popular")
    suspend fun fetchMovieList(
    ): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun fetchMovieDetail(
        @Path("movieId") movieId: Int
    ): MoviesDetails
}

