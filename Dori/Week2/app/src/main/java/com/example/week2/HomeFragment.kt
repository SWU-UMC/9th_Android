package com.example.week2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.week2.databinding.FragmentHomeBinding
import kotlin.jvm.java

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
    }


    private fun setupClickListener() {

        val albumTitle = binding.bottemPannelAlbumTv.text.toString()
        val albumSinger = binding.bottemPannelAlbumSuvTv.text.toString()

        binding.bottomPannelAlbumImgIv.setOnClickListener {
            val intent = Intent(context, AlbumActivity::class.java).apply {
                putExtra("title", albumTitle)
                putExtra("singer", albumSinger)
            }
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}