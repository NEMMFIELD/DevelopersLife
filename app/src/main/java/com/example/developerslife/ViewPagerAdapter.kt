package com.example.developerslife

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.developerslife.ui.BestFragment
import com.example.developerslife.ui.HotFragment
import com.example.developerslife.ui.LatestFragment

private const val NUM_TABS = 3
class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
       when (position)
       {
           0 -> return LatestFragment()
           1 -> return BestFragment()
           else -> return HotFragment()
       }
    }
}