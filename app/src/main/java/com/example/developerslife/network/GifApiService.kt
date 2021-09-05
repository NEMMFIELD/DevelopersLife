package com.example.developerslife.network

import com.example.developerslife.data.Model
import retrofit2.Response
import retrofit2.http.GET

interface GifApiService {
    @GET("random?json=true")
    suspend fun getGifs(): Response<Model>
}