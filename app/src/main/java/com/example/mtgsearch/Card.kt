package com.example.mtgsearch

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("image_uris") val imageUris: ImageUri
)

data class ImageUri(
    val large: String
)