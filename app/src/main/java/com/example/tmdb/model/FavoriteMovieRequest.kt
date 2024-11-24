package com.example.tmdb.model

data class FavoriteMovieRequest (
    val media_type: String = "movie",
    val media_id: Int,
    val favorite: Boolean
)