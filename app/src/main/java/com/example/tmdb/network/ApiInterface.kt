package com.example.tmdb.network

import com.example.tmdb.model.FavoriteMovieRequest
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.model.MoviesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("account/{account_id}/favorite/movies")
    suspend fun fetchFavoriteList(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): MoviesResponse

    @POST("account/{account_id}/favorite")
    suspend fun toggleFavroiteMovie(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body request: FavoriteMovieRequest
    )
}

