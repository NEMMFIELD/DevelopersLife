package com.example.developerslife.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.developerslife.data.Model
import com.example.developerslife.databinding.FragmentLatestBinding
import com.example.developerslife.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import android.annotation.SuppressLint

import com.bumptech.glide.request.RequestOptions

import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.example.developerslife.R


class LatestFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var myList: MutableList<Model> = mutableListOf()
    private var index: Int = 0
    private var _binding: FragmentLatestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLatestBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPrev.isEnabled = false
        binding.btnNext.setOnClickListener {
            index++
            if (index < myList.size) {
                displayPost(
                    myList[index].url!!,
                    myList[index].description!!
                )
            } else {
                scope.launch { loadPost() }
            }
            Log.d("INDEX", index.toString())
            // Log.d("PAGE",page.toString())
            if (myList.size > 0) binding.btnPrev.isEnabled = true
        }

        binding.btnPrev.setOnClickListener {
            index -= 1
            displayPost(myList[index].url!!, myList[index].description!!)
            if (index == 0) binding.btnPrev.isEnabled = false
        }

        scope.launch {
            try {
                loadPost()
            } catch (e: Exception) {
                Toast.makeText(context, "Seems like error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //При старте запускаем и загружаем запрос
    private suspend fun loadPost() {
        val response = RetrofitService().api.getGifs()
        if (response.isSuccessful) {
            response.body()?.url?.let {
                displayPost(
                    it,
                    response.body()?.description!!
                )
            }
            myList.add(response.body()!!)
            Log.d("SIZE", myList.size.toString())
        }
    }


    @SuppressLint("CheckResult")
    private fun displayPost(url: String, description: String) {
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 20f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        val requestOptions = RequestOptions()
        requestOptions.placeholder(circularProgressDrawable)
        requestOptions.error(R.drawable.error)
        requestOptions.skipMemoryCache(true)
        Glide.with(this)
            .load(url.replace("http", "https"))
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.gifurllatest)
        binding.textViewLatest.text = description
    }
    fun newInstance(list:ArrayList<Model>): LatestFragment {
        val args = Bundle()
        args.putParcelableArrayList("ARG", list)
        val fragment = LatestFragment()
        fragment.setArguments(args)
        return fragment
    }
}