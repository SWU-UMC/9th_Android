package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2week.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private var isMixOn = false // 토글 상태 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongBinding.inflate(inflater, container, false)

        // 토글 이미지 변경
        binding.songMixoffTg.setOnClickListener {
            toggleMix()
        }
        binding.songMixonTg.setOnClickListener {
            toggleMix()
        }

        return binding.root
    }

    // 토글 이미지 변경 클릭 리스너
    private fun toggleMix() {
        isMixOn = !isMixOn

        if(isMixOn){
            binding.songMixonTg.visibility = View.VISIBLE
            binding.songMixoffTg.visibility = View.GONE
        }else{
            binding.songMixonTg.visibility = View.GONE
            binding.songMixoffTg.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}