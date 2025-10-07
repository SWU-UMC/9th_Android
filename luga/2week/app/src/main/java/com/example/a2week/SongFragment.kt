package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

        // 수록곡 list
        val songList = (1..10).map{i ->
            SongListData(String.format("%02d", i), "노래 이름", "가수 이름")
        }

        val songAdapter = SongListAdapter(songList){ clickedSong ->
            Toast.makeText(requireContext(), "${clickedSong.title} 선택됨", Toast.LENGTH_SHORT).show()
        }

        binding.songListRv.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
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