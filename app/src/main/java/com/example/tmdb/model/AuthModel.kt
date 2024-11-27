package com.example.tmdb.model

import com.google.gson.annotations.SerializedName

data class RequestTokenResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("request_token") val requestToken: String
)

data class RequestToken(
    @SerializedName("request_token") val requestToken: String
)

data class SessionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("session_id") val sessionId: String
)

data class AccountResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String
)