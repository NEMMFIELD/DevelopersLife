package com.example.developerslifesupreme

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.developerslifesupreme.ui.BestFragment
import com.example.developerslifesupreme.ui.HotFragment
import com.example.developerslifesupreme.ui.LatestFragment
import com.example.developerslifesupreme.ui.RandomFragment

private const val NUM_TABS = 4
class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
       when (position)
       {
           0 -> return RandomFragment.newInstance()
           1 -> return LatestFragment()
           2 -> return BestFragment()
           else -> return HotFragment()
       }
    }
}