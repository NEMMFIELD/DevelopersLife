package com.example.developerslifesupreme.ui

import android.os.Bundle
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
import com.example.developerslifesupreme.databinding.FragmentBestBinding
import com.example.developerslifesupreme.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class BestFragment : Fragment() {
    private var currentPageBest = 0
    private val scope = CoroutineScope(Dispatchers.Main)
    private val bestList: MutableList<ResultItem?>? = mutableListOf()
    private var bestIndex = 0
    private var _binding: FragmentBestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            try {
                loadBestPosts(currentPageBest)
                displayPostBest(
                    bestList?.get(bestIndex)?.gifURL!!,
                    bestList[bestIndex]?.description!!
                )
            }
            catch (e:Exception)
            {
                doIfErr()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPrevBest.isEnabled = false
        binding.btnNextBest.setOnClickListener{
            bestIndex++
            if (bestIndex > 0) binding.btnPrevBest.isEnabled = true
                if (bestIndex < bestList!!.size) {
                    displayPostBest(bestList[bestIndex]?.gifURL!!, bestList[bestIndex]?.description!!)
                } else
                {
                    scope.launch {
                        try {
                            if (bestList.size % 5 == 0) {
                                currentPageBest++
                                loadBestPosts(currentPageBest)
                            }
                            displayPostBest(
                                bestList[bestIndex]?.gifURL!!,
                                bestList[bestIndex]?.description!!
                            )
                        }
                        catch (e:Exception)
                        {
                            doIfErr()
                        }
                    }
                }
        }
        binding.btnPrevBest.setOnClickListener {
            bestIndex--
            displayPostBest(bestList?.get(bestIndex)?.gifURL!!, bestList[bestIndex]?.description!!)
            if (bestIndex == 0) binding.btnPrevBest.isEnabled = false
        }
        binding.buttonRepeatBest.setOnClickListener {
            scope.launch {
                try {
                    loadBestPosts(currentPageBest)
                    displayPostBest(
                        bestList?.get(bestIndex)?.gifURL!!,
                        bestList[bestIndex]?.description!!
                    )
                }
                catch (e:Exception)
                {
                    doIfErr()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bestIndex = 0
        bestList?.clear()
        currentPageBest = 0
    }

    private suspend fun loadBestPosts(page: Int) {
        try {
            val response = RetrofitService().api.getGifsDaily(page).result
            if (response != null) {
                if (response.isNotEmpty()) {
                    binding.btnNextBest.visibility = View.VISIBLE
                    binding.btnPrevBest.visibility = View.VISIBLE
                    binding.buttonRepeatBest.visibility = View.GONE
                }
            }
            for (i in 0..4) {
                bestList?.add(response?.get(i)!!)
            }
            println("Size of Bestlist: ${bestList?.size}")
        }
        catch (e: Exception)
        {
           // println(e)
          doIfErr()
        }
    }

    private fun displayPostBest(url: String, description: String) {
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
            .into(binding.gifurlBest)
        binding.textViewGifBest.text = description
    }
    private fun doIfErr()
    {
        binding.gifurlBest.setImageResource(R.drawable.error128)
        binding.textViewGifBest.text = "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
        binding.btnNextBest.visibility = View.GONE
        binding.btnPrevBest.visibility = View.GONE
        binding.buttonRepeatBest.visibility = View.VISIBLE
    }
}