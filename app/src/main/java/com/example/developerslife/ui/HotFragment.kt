package com.example.developerslife.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.developerslife.databinding.FragmentHotBinding


class HotFragment : Fragment() {
    private var _binding : FragmentHotBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }
}