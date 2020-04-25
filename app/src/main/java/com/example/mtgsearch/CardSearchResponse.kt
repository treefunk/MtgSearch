package com.example.mtgsearch

import com.google.gson.annotations.SerializedName

data class CardSearchResponse(
    @SerializedName("total_values") val totalValues: String,
    val data : List<Card>
)

