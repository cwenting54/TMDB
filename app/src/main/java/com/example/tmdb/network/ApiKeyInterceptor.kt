package com.example.tmdb.network

import com.example.tmdb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlWithApiKey = request.url.newBuilder().addQueryParameter("api_key", apiKey).build()
        val newRequest = request.newBuilder()
            .url(urlWithApiKey)
            .build()
        return chain.proceed(newRequest)
    }
}