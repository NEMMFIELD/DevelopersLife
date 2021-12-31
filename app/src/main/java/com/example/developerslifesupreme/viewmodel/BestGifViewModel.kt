package com.example.developerslifesupreme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.developerslifesupreme.data.ResultItem
import com.example.developerslifesupreme.network.GifsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BestGifViewModel(private val repository: GifsRepo):ViewModel() {
    private val _mutableBestGif = MutableLiveData<List<ResultItem>>(emptyList())
    val bestGif : LiveData<List<ResultItem>> get() = _mutableBestGif

    suspend fun fetchBestGif(page:Int)
    {
        try
        {
            val newGif = repository.getBestGifs(page = page)
            val updatedGifList =_mutableBestGif.value?.plus(newGif).orEmpty()
            _mutableBestGif.value = updatedGifList
        }
        catch (e:Exception)
        {
            println(e)
        }

    }
}
class BestGifViewModelFactory(private val repository: GifsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BestGifViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BestGifViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}