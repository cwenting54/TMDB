package com.example.tmdb.network

import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.model.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("movie/popular")
    fun fetchMovieList(
        @Query("page") page: Int? = null
    ): Single<List<MoviesResponse>>

    @GET("movie/{movieId}")
    fun fetchMovieDetail(
        @Path("movieId") movieId: Int
    ): Single<MoviesDetails>
}

