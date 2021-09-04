package com.example.developerslife.ui
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
import com.example.developerslife.R
import com.example.developerslife.data.Model
import com.example.developerslife.databinding.FragmentLatestBinding
import com.example.developerslife.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        //Inflate the layout for this fragment
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
            if (myList.size > 0) binding.btnPrev.isEnabled = true
        }
        binding.btnPrev.setOnClickListener {
            index -= 1
            displayPost(myList[index].url!!, myList[index].description!!)
            if (index == 0) binding.btnPrev.isEnabled = false
        }
        binding.buttonRepeat.setOnClickListener { scope.launch { loadPost() } }
        scope.launch {
            try {
                loadPost()
            } catch (e: Exception) {
               // Toast.makeText(context, "Seems like error", Toast.LENGTH_SHORT).show()
                binding.btnNext.visibility = View.GONE
                binding.btnPrev.visibility = View.GONE
                binding.gifurllatest.setImageResource(R.drawable.networkerror)
                binding.textViewLatest.text = getString(R.string.network_error)
                binding.buttonRepeat.visibility = View.VISIBLE
            }
        }
    }

    //При старте запускаем и загружаем запрос
    private suspend fun loadPost() {
        val response = RetrofitService().api.getGifs()
        if (response.isSuccessful) {
            binding.btnNext.visibility = View.VISIBLE
            binding.btnPrev.visibility = View.VISIBLE
            binding.buttonRepeat.visibility = View.GONE
            response.body()?.url?.let {
                displayPost(
                    it,
                    response.body()?.description!!
                )
            }
            myList.add(response.body()!!)
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

    fun newInstance(list: ArrayList<Model>): LatestFragment {
        val args = Bundle()
        args.putParcelableArrayList("ARG", list)
        val fragment = LatestFragment()
        fragment.arguments = args
        return fragment
    }
}