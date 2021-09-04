package com.example.developerslife.network

import com.example.developerslife.data.FinalList
import com.example.developerslife.data.Model
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GifApiService {
    @GET("random?json=true")
    suspend fun getGifs(): Response<Model>
}