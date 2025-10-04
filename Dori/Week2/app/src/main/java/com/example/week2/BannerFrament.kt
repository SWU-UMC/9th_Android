package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week2.databinding.FrgmentBannerBinding


class BannerFragment(val imgRes : Int) : Fragment() {

    lateinit var  binding : FrgmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrgmentBannerBinding.inflate(inflater, container ,false)

        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }
}