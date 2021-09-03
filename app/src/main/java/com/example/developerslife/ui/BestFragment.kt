package com.example.developerslife.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.developerslife.R
import com.example.developerslife.databinding.FragmentBestBinding
import com.example.developerslife.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class BestFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var _binding : FragmentBestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBestBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch {
            try {
                val response = RetrofitService().api.getGifsDaily()


                if (response.isSuccessful) {
                    displayPost(response.body()!!.list[0].url!!, response.body()!!.list[0].url!!)
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(context, "Seems like error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayPost(url:String,description:String)
    {
        Glide.with(this)
            .load(url.replace("http","https"))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.gifUrlBest)
        binding.textViewBest.text = description
    }
}