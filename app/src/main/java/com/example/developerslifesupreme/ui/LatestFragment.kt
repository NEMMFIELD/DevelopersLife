package com.example.developerslifesupreme.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.developerslifesupreme.Utils.Companion.page
import com.example.developerslifesupreme.databinding.FragmentLatestBinding
import com.example.developerslifesupreme.viewmodel.LatestGifViewModel
import com.example.developerslifesupreme.viewmodel.LatestGifViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match

class LatestFragment : Fragment() {
    private lateinit var viewModel: LatestGifViewModel
    private val scope = CoroutineScope(Dispatchers.Main)
    private var localIndex: Int = 0
    private var _binding: FragmentLatestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLatestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            LatestGifViewModelFactory((requireActivity() as MainActivity).repository)
        ).get(LatestGifViewModel::class.java)
        scope.launch { loadPost() }
        binding.btnPrevLatest.isEnabled = false
        binding.btnNextLatest.setOnClickListener {
            localIndex++
            if (localIndex > 0) binding.btnPrevLatest.isEnabled = true

            if (localIndex < viewModel.latestGif.value!!.size) {
                displayPost(
                    viewModel.latestGif.value!![localIndex].gifURL!!,
                    viewModel.latestGif.value!![localIndex].description!!
                )
            } else {
                scope.launch {
                    try {
                        if (viewModel.latestGif.value!!.size % 5 == 0) {
                            page++
                            loadPost()
                            displayPost(
                                viewModel.latestGif.value!![localIndex].gifURL!!,
                                viewModel.latestGif.value!![localIndex].description!!
                            )
                        }
                    }
                    catch (e: Exception) {
                        doIfErr()
                    }
                }
            }
            Log.d("INDEX", localIndex.toString())
        }

        binding.btnPrevLatest.setOnClickListener {
            localIndex -= 1
            displayPost(
                viewModel.latestGif.value?.get(localIndex)?.gifURL!!,
                viewModel.latestGif.value!![localIndex].description!!
            )
            Log.d("INDEX", localIndex.toString())
            if (localIndex == 0) binding.btnPrevLatest.isEnabled = false
        }

        binding.btnRepeatLatest.setOnClickListener {
            scope.launch {
                try {
                    loadPost()
                    displayPost(
                        viewModel.latestGif.value?.get(localIndex)?.gifURL!!,
                        viewModel.latestGif.value?.get(localIndex)?.description!!
                    )
                } catch (e: Exception) {
                    doIfErr()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        localIndex = 0
    }

    //При старте загружаем и показываем запрос
    private suspend fun loadPost() {
        try {
            viewModel.fetchLatestGif(page)
            viewModel.latestGif.value?.get(localIndex)
                ?.let {
                    it.gifURL?.let { it1 ->
                        viewModel.latestGif.value!![localIndex].let { it2 ->
                            it2.description?.let { it3 ->
                                displayPost(
                                    it1,
                                    it3
                                )
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            println("Error is $e")
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
        requestOptions.error(R.drawable.error128)
        requestOptions.skipMemoryCache(true)
        Glide.with(this)
            .load(url.replace("http", "https"))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gifurlLatest)
        binding.textViewGifLatest.text = description
    }

    private fun doIfErr() {
        binding.gifurlLatest.setImageResource(R.drawable.error128)
        binding.textViewGifLatest.text =
            "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
        hideNextButton()
        hidePrevButton()
        showRepeatButton()
    }

    private fun showPrevButton() {
        binding.btnPrevLatest.visibility = View.VISIBLE
    }

    private fun hidePrevButton() {
        binding.btnPrevLatest.visibility = View.INVISIBLE
    }

    private fun showNextButton() {
        binding.btnNextLatest.visibility = View.VISIBLE
    }

    private fun hideNextButton() {
        binding.btnNextLatest.visibility = View.INVISIBLE
    }

    private fun showRepeatButton() {
        binding.btnRepeatLatest.visibility = View.VISIBLE
    }

    private fun hideRepeatButton() {
        binding.btnRepeatLatest.visibility = View.GONE
    }
}
