package com.example.developerslife.network

import com.example.developerslife.data.FinalList
import com.example.developerslife.data.Model
import retrofit2.Response
import retrofit2.http.GET

interface GifApiService {
    @GET("latest/0?json=true")
    suspend fun getGifs(): Response<FinalList>
    @GET("daily/0?json=true")
    suspend fun getGifsDaily(): Response<FinalList>
}