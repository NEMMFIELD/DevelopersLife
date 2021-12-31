package com.example.developerslifesupreme.network

import android.content.Context
import com.example.developerslifesupreme.data.ResultItem

interface Repository {
   suspend fun  getRandomGif():ResultItem?
    suspend fun getLatestGifs(page: Int):List<ResultItem>
   suspend fun getBestGifs(page:Int):List<ResultItem?>?
}

class GifsRepo(context: Context):Repository
{
    override suspend fun getRandomGif(): ResultItem? {
        return RetrofitService().api.getRandomGifs().body()
    }

    override suspend fun getLatestGifs(page:Int): List<ResultItem> {
        return RetrofitService() .api.getGifs(page = page).result
    }

    override suspend fun getBestGifs(page:Int): List<ResultItem> {
       return RetrofitService().api.getGifsDaily(page).result
    }
}