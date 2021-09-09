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
import com.example.developerslifesupreme.databinding.FragmentHotBinding
import com.example.developerslifesupreme.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HotFragment : Fragment() {
    private var currentPageHot = 0
    private val scope = CoroutineScope(Dispatchers.Main)
    private val hotList: MutableList<ResultItem?> = mutableListOf()
    private var hotIndex = 0
    private var _binding: FragmentHotBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            try {
                loadHotPosts(currentPageHot)
                displayPostHot(
                    hotList.get(hotIndex)?.gifURL!!,
                    hotList[hotIndex]?.description!!
                )
            } catch (e: Exception) {
                doIfErr()
            }
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPrevHot.isEnabled = false
        binding.btnNextHot.setOnClickListener {
            hotIndex++
            if (hotIndex > 0) binding.btnPrevHot.isEnabled = true
            if (hotList != null) {
                if (hotIndex < hotList.size) {
                    displayPostHot(hotList[hotIndex]?.gifURL!!, hotList[hotIndex]?.description!!)
                } else {
                    scope.launch {
                        try {
                            if (hotList.size % 5 == 0) {
                                currentPageHot++
                                loadHotPosts(currentPageHot)
                            }
                            displayPostHot(
                                hotList[hotIndex]?.gifURL!!,
                                hotList[hotIndex]?.description!!
                            )
                        } catch (e: Exception) {
                            doIfErr()
                        }
                    }

                }
            }
        }
        binding.btnPrevHot.setOnClickListener {
            hotIndex--
            displayPostHot(hotList?.get(hotIndex)?.gifURL!!, hotList[hotIndex]?.description!!)
            if (hotIndex == 0) binding.btnPrevHot.isEnabled = false
        }
        binding.buttonRepeatHot.setOnClickListener {
            scope.launch {
                try {
                    loadHotPosts(currentPageHot)
                    displayPostHot(
                        hotList?.get(hotIndex)?.gifURL!!,
                        hotList[hotIndex]?.description!!
                    )
                } catch (e: java.lang.Exception) {
                    doIfErr()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        hotIndex = 0
        hotList.clear()
        currentPageHot = 0
    }

    private suspend fun loadHotPosts(page: Int) {
        try {
            val response = RetrofitService().api.getGifsHot(currentPageHot).result
            if (response != null) {
                if (response.isNotEmpty()) {
                    binding.btnNextHot.visibility = View.VISIBLE
                    binding.btnPrevHot.visibility = View.VISIBLE
                    binding.buttonRepeatHot.visibility = View.GONE
                }
            }
            for (i in 0..4) {
                response?.get(i)?.let { hotList?.add(it) }
            }
        } catch (e: Exception) {
            doIfErr()
        }
    }

    private fun displayPostHot(url: String, description: String) {
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
            .into(binding.gifurlHot)
        binding.textViewGifHot.text = description
    }

    private fun doIfErr() {
        binding.gifurlHot.setImageResource(R.drawable.error128)
        binding.textViewGifHot.text = "Произошла ошибка при загрузке данных."
        binding.btnNextHot.visibility = View.GONE
        binding.btnPrevHot.visibility = View.GONE
        binding.buttonRepeatHot.visibility = View.VISIBLE
    }
}