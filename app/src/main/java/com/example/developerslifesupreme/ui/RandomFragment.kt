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
import com.example.developerslifesupreme.databinding.FragmentRandomBinding
import com.example.developerslifesupreme.viewmodel.GifViewModel
import com.example.developerslifesupreme.viewmodel.GifViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RandomFragment : Fragment() {
    private lateinit var viewModel: GifViewModel
    private val scope = CoroutineScope(Dispatchers.Main)
    private var index: Int = 0
    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        binding.btnPrevRandom.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            GifViewModelFactory((requireActivity() as MainActivity).repository)
        ).get(GifViewModel::class.java)
        //Загружаемся со старта
        scope.launch {
            try {
                loadPost()
            } catch (e: Exception) {
                doIfErr()
            }
        }

        binding.btnNextRandom.setOnClickListener {
            index++
            if (index < viewModel.randomGif.value!!.size) {
                displayPost(
                    viewModel.randomGif.value!![index].gifURL!!,
                    viewModel.randomGif.value!![index].description!!
                )
            } else {
                scope.launch {
                    try {
                        loadPost()
                    } catch (e: Exception) {
                        println("Error is: $e")
                    }

                }
            }
            if (viewModel.randomGif.value!!.isNotEmpty()) binding.btnPrevRandom.isEnabled = true
        }
        binding.btnPrevRandom.setOnClickListener {
            index--
            viewModel.randomGif.value?.get(index)?.description?.let { it1 ->
                viewModel.randomGif.value!![index].gifURL?.let { it2 ->
                    displayPost(
                        it2,
                        it1
                    )
                }
            }
            if (index == 0) binding.btnPrevRandom.isEnabled = false
        }
        binding.btnRepeatRandom.setOnClickListener {
            scope.launch {
                try {
                    loadPost()
                } catch (e: Exception) {
                    doIfErr()
                }
            }
        }

    }

    private suspend fun loadPost() {
        viewModel.fetchRandomGif()
        binding.btnNextRandom.visibility = View.VISIBLE
        binding.btnPrevRandom.visibility = View.VISIBLE
        binding.btnRepeatRandom.visibility = View.GONE
        viewModel.randomGif.value?.get(index)
            ?.let {
                it.gifURL?.let { it1 ->
                    viewModel.randomGif.value!![index].let { it2 ->
                        it2.description?.let { it3 ->
                            displayPost(
                                it1,
                                it3
                            )
                        }
                    }
                }
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
        Glide.with(requireContext())
            .load(url.replace("http", "https"))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gifurlRandom)
        binding.textViewGifRandom.text = description
    }

    companion object {
        fun newInstance() = RandomFragment()
    }

    private fun doIfErr() {
        binding.gifurlRandom.setImageResource(R.drawable.error128)
        binding.textViewGifRandom.text =
            "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
        hideNextButton()
        hidePrevButton()
        showRepeatButton()
        hideProgressBar()
    }

    private fun showPrevButton() {
        binding.btnPrevRandom.visibility = View.VISIBLE
    }

    private fun hidePrevButton() {
        binding.btnPrevRandom.visibility = View.INVISIBLE
    }

    private fun showNextButton() {
        binding.btnNextRandom.visibility = View.VISIBLE
    }

    private fun hideNextButton() {
        binding.btnNextRandom.visibility = View.INVISIBLE
    }

    private fun showRepeatButton() {
        binding.btnRepeatRandom.visibility = View.VISIBLE
    }

    private fun hideRepeatButton() {
        binding.btnRepeatRandom.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressCircular.visibility = View.INVISIBLE
    }

}
