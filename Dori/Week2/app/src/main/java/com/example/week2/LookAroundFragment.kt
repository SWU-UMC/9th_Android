package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week2.databinding.FragmentLookAroundBinding

class LookAroundFragment : Fragment() {

    lateinit var binding: FragmentLookAroundBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookAroundBinding.inflate(inflater, container, false)
        return binding.root
    }
}