package com.example.developerslifesupreme.network

import com.example.developerslifesupreme.data.Response
import com.example.developerslifesupreme.data.ResultItem
import retrofit2.http.GET
import retrofit2.http.Path

interface GifApiService {
    @GET("random?json=true")
    suspend fun getRandomGifs(): retrofit2.Response<ResultItem>

    @GET("latest/{page}?json=true")
    suspend fun getGifs( @Path("page") page: Int): Response

    @GET("top/{page}?json=true")
    suspend fun getGifsDaily(@Path("page")page:Int): Response

    @GET("hot/{page}?json=true")
    suspend fun getGifsHot(@Path("page")page:Int): Response

}