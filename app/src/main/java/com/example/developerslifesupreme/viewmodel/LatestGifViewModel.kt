package com.example.developerslifesupreme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.developerslifesupreme.Utils.Companion.page
import com.example.developerslifesupreme.data.ResultItem
import com.example.developerslifesupreme.network.GifsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LatestGifViewModel(private val repository: GifsRepo):ViewModel() {
    private val _mutableLatestGif = MutableLiveData<List<ResultItem>>(emptyList())
    val latestGif : LiveData<List<ResultItem>> get() = _mutableLatestGif


    suspend fun fetchLatestGif(page:Int)
    {
        try{
            val newGif = repository.getLatestGifs(page = page)
            val updatedGifList =_mutableLatestGif.value?.plus(newGif).orEmpty()
            _mutableLatestGif.value = updatedGifList
        }
        catch (e:Exception)
        {
            println(e)
        }

    }
}
class LatestGifViewModelFactory(private val repository: GifsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LatestGifViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LatestGifViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}