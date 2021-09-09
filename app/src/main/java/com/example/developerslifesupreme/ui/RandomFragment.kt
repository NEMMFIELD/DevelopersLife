package com.example.developerslifesupreme.ui

import android.annotation.SuppressLint
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
import com.example.developerslifesupreme.databinding.FragmentRandomBinding
import com.example.developerslifesupreme.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RandomFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var myList: MutableList<ResultItem?>? = ArrayList()
    private var index: Int = 0
    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPrevRandom.isEnabled = false
        binding.btnNextRandom.setOnClickListener {
            index++
            if (index < myList?.size!!) {
                displayPost(
                    myList?.get(index)?.gifURL!!,
                    myList?.get(index)?.description!!
                )
            } else {
                scope.launch { loadPost() }
            }
            if (myList?.size!! > 0) binding.btnPrevRandom.isEnabled = true
        }
        binding.btnPrevRandom.setOnClickListener {
            index--
            myList?.get(index)?.description?.let { it1 ->
                displayPost(
                    myList?.get(index)?.gifURL!!,
                    it1
                )
            }
            if (index == 0) binding.btnPrevRandom.isEnabled = false
        }
        binding.buttonRepeatRandom.setOnClickListener { scope.launch { loadPost() } }
        scope.launch {
            try {
                loadPost()
            } catch (e: Exception) {
                binding.btnNextRandom.visibility = View.GONE
                binding.btnPrevRandom.visibility = View.GONE
                binding.gifurlRandom.setImageResource(R.drawable.error128)
                binding.textViewGifRandom.text =
                    "Произошла ошибка при загрузке данных. Проверьте подключение к сети"
                binding.buttonRepeatRandom.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun loadPost() {
        val response = RetrofitService().api.getRandomGifs()
        if (response.isSuccessful) {
            binding.btnNextRandom.visibility = View.VISIBLE
            binding.btnPrevRandom.visibility = View.VISIBLE
            binding.buttonRepeatRandom.visibility = View.GONE
            response.body()?.gifURL?.let {
                displayPost(
                    it,
                    response.body()?.description!!
                )
            }
            myList?.add(response.body()!!)
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
            .into(binding.gifurlRandom)
        binding.textViewGifRandom.text = description
    }

    companion object {
        fun newInstance() = RandomFragment()
    }
}