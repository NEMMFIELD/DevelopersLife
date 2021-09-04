package com.example.developerslife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.example.developerslife.data.Model
import com.example.developerslife.databinding.ActivityMainBinding
import com.example.developerslife.ui.LatestFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, LatestFragment().newInstance(ArrayList()))
                .commit()
        }
        supportActionBar?.title = Html.fromHtml("<font color='#000000'>Developers Life</font>");
    }
}