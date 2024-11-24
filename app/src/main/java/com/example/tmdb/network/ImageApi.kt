package com.example.tmdb.network

import com.example.tmdb.BuildConfig

object ImageApi {
    fun getImageUrl(path: String?, size: ImageSize? = ImageSize.ORIGINAL) =
        "${BuildConfig.IMAGE_URL}${size}/${path}"
}

enum class ImageSize(val path: String) {
    W100("w100"),
    W250("w250"),
    W500("w500"),
    W750("w750"),
    ORIGINAL("original");

    override fun toString() = path
}