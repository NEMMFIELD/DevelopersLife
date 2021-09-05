package com.example.developerslife

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.developerslife.databinding.ActivityMainBinding
import com.example.developerslife.ui.GifFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var myFragment: GifFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = Html.fromHtml("<font color='#000000'>Developers Life</font>")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            myFragment = GifFragment.newInstance()
            supportFragmentManager.beginTransaction()

                .replace(R.id.fragment_container_view, myFragment!!)
                .commit()
        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        supportFragmentManager.putFragment(outState, "GifFragment", myFragment!!)
    }
}