package com.example.tmdb.network

import com.example.tmdb.model.AccountResponse
import com.example.tmdb.model.FavoriteMovieRequest
import com.example.tmdb.model.MoviesDetails
import com.example.tmdb.model.MoviesResponse
import com.example.tmdb.model.RequestToken
import com.example.tmdb.model.RequestTokenResponse
import com.example.tmdb.model.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @POST("authentication/session/new")
    suspend fun createSession(@Body request: RequestToken): SessionResponse

    @GET("account")
    suspend fun getAccountDetails(@Query("session_id") sessionId: String): AccountResponse

    @GET("movie/popular")
    suspend fun fetchMovieList(
        @Query("page") page: Int
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

