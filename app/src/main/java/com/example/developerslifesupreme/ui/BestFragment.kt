package com.example.developerslifesupreme.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.developerslifesupreme.MainActivity
import com.example.developerslifesupreme.R
import com.example.developerslifesupreme.Utils.Companion.bestPage
import com.example.developerslifesupreme.databinding.FragmentBestBinding
import com.example.developerslifesupreme.viewmodel.BestGifViewModel
import com.example.developerslifesupreme.viewmodel.BestGifViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BestFragment : Fragment() {
    private lateinit var bestViewModel: BestGifViewModel
    private val scope = CoroutineScope(Dispatchers.Main)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bestViewModel = ViewModelProvider(
            this,
            BestGifViewModelFactory((requireActivity() as MainActivity).repository)
        ).get(BestGifViewModel::class.java)
        scope.launch { loadBestPosts() }
        binding.btnPrevBest.isEnabled = false
        binding.btnNextBest.setOnClickListener {
            bestIndex++
            if (bestIndex > 0) binding.btnPrevBest.isEnabled = true
            if (bestIndex < bestViewModel.bestGif.value!!.size) {
                displayPostBest(
                    bestViewModel.bestGif.value?.get(bestIndex)?.gifURL!!,
                    bestViewModel.bestGif.value?.get(bestIndex)?.description!!
                )
            } else {
                scope.launch {
                    try {
                        if (bestViewModel.bestGif.value!!.size % 5 == 0) {
                            bestPage++
                            loadBestPosts()
                            displayPostBest(
                                bestViewModel.bestGif.value?.get(bestIndex)?.gifURL!!,
                                bestViewModel.bestGif.value?.get(bestIndex)?.description!!
                            )
                        }
                    } catch (e: Exception) {
                        doIfErr()
                    }
                }
            }
        }
        binding.btnPrevBest.setOnClickListener {
            bestIndex--
            displayPostBest(
                bestViewModel.bestGif.value?.get(bestIndex)?.gifURL!!,
                bestViewModel.bestGif.value?.get(bestIndex)?.description!!
            )
            if (bestIndex == 0) binding.btnPrevBest.isEnabled = false
        }
        binding.btnRepeatBest.setOnClickListener {
            scope.launch {
                try {
                    loadBestPosts()
                    displayPostBest(
                        bestViewModel.bestGif.value?.get(bestIndex)?.gifURL!!,
                        bestViewModel.bestGif.value?.get(bestIndex)?.description!!
                    )
                } catch (e: Exception) {
                    doIfErr()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bestIndex = 0
    }

    private suspend fun loadBestPosts() {
        try {
            bestViewModel.fetchBestGif(bestPage)
            bestViewModel.bestGif.value?.get(bestIndex)
                ?.let {
                    it.gifURL?.let { it1 ->
                        bestViewModel.bestGif.value!![bestIndex].let { it2 ->
                            it2.description?.let { it3 ->
                                displayPostBest(
                                    it1,
                                    it3
                                )
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            doIfErr()
        }
    }

    @SuppressLint("CheckResult")
    private fun displayPostBest(url: String, description: String) {
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()

        val requestOptions = RequestOptions()
        requestOptions.placeholder(circularProgressDrawable)
        requestOptions.error(R.drawable.error128)
        requestOptions.skipMemoryCache(true)
        Glide.with(this)
            .load(url.replace("http", "https"))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gifurlBest)
        binding.textViewGifBest.text = description
    }

    private fun doIfErr() {
        binding.gifurlBest.setImageResource(R.drawable.error128)
        binding.textViewGifBest.text =
            "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
        hideNextButton()
        hidePrevButton()
        showRepeatButton()
        hideProgressBar()
    }

    private fun showPrevButton() {
        binding.btnPrevBest.visibility = View.VISIBLE
    }

    private fun hidePrevButton() {
        binding.btnPrevBest.visibility = View.INVISIBLE
    }

    private fun showNextButton() {
        binding.btnNextBest.visibility = View.VISIBLE
    }

    private fun hideNextButton() {
        binding.btnNextBest.visibility = View.INVISIBLE
    }

    private fun showRepeatButton() {
        binding.btnRepeatBest.visibility = View.VISIBLE
    }

    private fun hideRepeatButton() {
        binding.btnRepeatBest.visibility = View.GONE
    }

    private fun hideProgressBar()
    {
        binding.progressCircularBest.visibility = View.INVISIBLE
    }
}