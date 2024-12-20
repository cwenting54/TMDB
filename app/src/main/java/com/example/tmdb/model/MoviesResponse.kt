package com.example.tmdb.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MoviesDetails>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

