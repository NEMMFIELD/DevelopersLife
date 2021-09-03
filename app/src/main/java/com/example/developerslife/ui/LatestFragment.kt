package com.example.developerslife.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.developerslife.R
import com.example.developerslife.databinding.FragmentLatestBinding
import com.example.developerslife.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import javax.xml.transform.Source

// TODO: Rename parameter arguments, choose names that match

class LatestFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.Main)

    private var _binding : FragmentLatestBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLatestBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.textViewLatest.text = "Super Kendra Lust"
      scope.launch {
          try {
              val response = RetrofitService().api.getGifs()


                  if (response.isSuccessful) {
                    /*  Glide.with(view)
                          .load(response.body()!!.url?.replace("http","https"))
                          .placeholder(R.drawable.ic_launcher_background)
                          .error(R.drawable.ic_launcher_foreground)
                          .into(binding.gifurllatest)
                      binding.textViewLatest.text = response.body()!!.description*/
                      displayPost(response.body()!!.list[0].url!!, response.body()!!.list[0].description!!)
                  }
          }
          catch (e:Exception)
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
                          .into(binding.gifurllatest)
                      binding.textViewLatest.text = description
    }
}