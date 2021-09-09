package com.example.developerslifesupreme.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.developerslifesupreme.R
import com.example.developerslifesupreme.data.ResultItem
import com.example.developerslifesupreme.databinding.FragmentLatestBinding
import com.example.developerslifesupreme.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match

class LatestFragment : Fragment() {
    private var currentPage = 0
    private val scope = CoroutineScope(Dispatchers.Main)
    private val myList: MutableList<ResultItem?>? = mutableListOf()
    private var localIndex: Int = 0
    private var _binding: FragmentLatestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment
        _binding = FragmentLatestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            try {
                loadPost(currentPage)
                displayPost(myList?.get(localIndex)?.gifURL!!, myList[localIndex]?.description!!)
            }
            catch (e: Exception)
            {
               doIfErr()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPrevLatest.isEnabled = false
        binding.btnNextLatest.setOnClickListener {
            localIndex++
            if (localIndex > 0) binding.btnPrevLatest.isEnabled = true
            if (myList != null) {
                if (localIndex < myList.size) {
                    displayPost(
                        myList.get(localIndex)?.gifURL!!,
                        myList[localIndex]?.description!!
                    )
                }
                else {
                    scope.launch {
                        try {
                            if (myList.size % 5 == 0) {
                                currentPage++
                                loadPost(currentPage)
                                displayPost(
                                    myList[localIndex]?.gifURL!!,
                                    myList[localIndex]?.description!!
                                )
                            }
                        }
                        catch (e:Exception)
                        {
                           doIfErr()
                        }
                    }
                }
            }
             Log.d("INDEX", localIndex.toString())
        }

        binding.btnPrevLatest.setOnClickListener {
            localIndex -= 1
            displayPost(
                myList?.get(localIndex)?.gifURL!!,
                myList[localIndex]?.description!!
            )
            Log.d("INDEX", localIndex.toString())
            if (localIndex == 0) binding.btnPrevLatest.isEnabled = false
        }

        binding.buttonRepeatLatest.setOnClickListener {
            scope.launch {
                try {
                    loadPost(currentPage)
                    displayPost(
                        myList?.get(localIndex)?.gifURL!!,
                        myList[localIndex]?.description!!
                    )
                }
                catch (e:Exception)
                {
                    doIfErr()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        localIndex = 0
        myList?.clear()
        currentPage = 0
    }
    //При старте запускаем и загружаем запрос
    private suspend fun loadPost(page: Int) {
        try {
            val response = RetrofitService().api.getGifs(page).result
            if (response != null) {
                if (response.isNotEmpty()) {
                    binding.btnNextLatest.visibility = View.VISIBLE
                    binding.btnPrevLatest.visibility = View.VISIBLE
                    binding.buttonRepeatLatest.visibility = View.GONE
                }
            }
            for (i in 0..4) {
                myList?.add(response?.get(i)!!)
            }
            println("Size of list: ${myList?.size}")
        }
        catch (e: Exception)
        {
           doIfErr()
        }
    }

    @SuppressLint("CheckResult")
    private fun displayPost(url: String, description: String) {
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()

        val requestOptions = RequestOptions()
        requestOptions.placeholder(circularProgressDrawable)
        requestOptions.error(R.drawable.error128).fitCenter()
        requestOptions.skipMemoryCache(true)
        requestOptions.centerCrop()
        Glide.with(this)
            .load(url.replace("http", "https"))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gifurlLatest)
        binding.textViewGifLatest.text = description
    }
    private fun doIfErr()
    {
        binding.gifurlLatest.setImageResource(R.drawable.error128)
        binding.textViewGifLatest.text = "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
        binding.btnNextLatest.visibility = View.GONE
        binding.btnPrevLatest.visibility = View.GONE
        binding.buttonRepeatLatest.visibility = View.VISIBLE
    }
}
