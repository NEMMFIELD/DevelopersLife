package com.example.developerslifesupreme

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.developerslifesupreme.databinding.ActivityMainBinding
import com.example.developerslifesupreme.network.GifsRepo
import com.google.android.material.tabs.TabLayoutMediator

val tabs = arrayOf(
    "Случайные",
    "Последние",
    "Лучшие"
)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var repository:GifsRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = GifsRepo(applicationContext)
        supportActionBar?.title = Html.fromHtml("<font color='#000000'>Developers Life</font>");
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }
}