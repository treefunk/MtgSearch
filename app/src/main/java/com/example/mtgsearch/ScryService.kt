package com.example.mtgsearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScryService {

    @GET("/cards/autocomplete")
    fun autoCompCards(@Query("q") q: String): Call<Response>

    @GET("/cards/search")
    fun getCardByName(@Query("q") q: String): Call<CardSearchResponse>

}