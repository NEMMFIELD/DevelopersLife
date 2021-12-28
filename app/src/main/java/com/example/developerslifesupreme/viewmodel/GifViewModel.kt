package com.example.developerslifesupreme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.developerslifesupreme.data.ResultItem
import com.example.developerslifesupreme.network.GifsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GifViewModel(private val repository: GifsRepo):ViewModel() {
    private val _mutableRandomGif = MutableLiveData<List<ResultItem>>(emptyList())
     val randomGif :LiveData<List<ResultItem>> get() = _mutableRandomGif
    val scope = CoroutineScope(Dispatchers.Main)


   suspend fun fetchRandomGif()
    {
        try{
            val newGif = repository.getRandomGif()
            val updatedGifList =_mutableRandomGif.value?.plus(newGif).orEmpty()
            _mutableRandomGif.value = updatedGifList as List<ResultItem>?
        }
        catch (e:Exception)
        {
            println(e)
        }

    }
}
class GifViewModelFactory(private val repository: GifsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GifViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GifViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}